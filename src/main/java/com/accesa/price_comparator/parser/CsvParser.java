package com.accesa.price_comparator.parser;

import com.accesa.price_comparator.entities.Discount;
import com.accesa.price_comparator.entities.Product;
import com.accesa.price_comparator.entities.ProductPrice;
import com.accesa.price_comparator.repositories.DiscountRepository;
import com.accesa.price_comparator.repositories.ProductPriceRepository;
import com.accesa.price_comparator.repositories.ProductRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvParser {
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final DiscountRepository discountRepository;

    // Configuration for semicolon-delimited CSVs
    private final CSVParser csvParser = new CSVParserBuilder()
            .withSeparator(';')
            .withIgnoreQuotations(true)
            .build();

    public void parseProductCsv(InputStream is, String store, LocalDate date) throws Exception {
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(is))
                .withCSVParser(csvParser)
                .build()) {

            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);

                // Now expect 7 columns instead of 8
                if (row.length < 7) {
                    log.warn("Skipping malformed product row: {}", Arrays.toString(row));
                    continue;
                }

                try {
                    Product product = new Product();
                    product.setProductId(safeGet(row, 0));
                    product.setProductName(safeGet(row, 1).trim().toLowerCase());
                    product.setCategory(safeGet(row, 2));
                    product.setBrand(safeGet(row, 3));
                    productRepository.save(product);

                    ProductPrice price = new ProductPrice();
                    price.setProduct(product);
                    price.setStore(store);
                    price.setDate(date);
                    price.setPackageQuantity(parseDoubleSafe(safeGet(row, 4)));
                    price.setPackageUnit(safeGet(row, 5));
                    price.setPrice(parseDoubleSafe(safeGet(row, 6))); // Moved from index 7
                    productPriceRepository.save(price);
                } catch (Exception e) {
                    log.error("Error processing row: {}", e.getMessage());
                }
            }
        }
    }

    public void parseDiscountCsv(InputStream is, String store) throws Exception {
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(is))
                .withCSVParser(csvParser)
                .build()) {

            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) { // Skip header row
                String[] row = rows.get(i);

                if (row.length < 9) {
                    log.warn("Skipping malformed discount row in {}: {}", store, Arrays.toString(row));
                    continue;
                }

                try {
                    Product product = productRepository.findById(safeGet(row, 0))
                            .orElseThrow(() -> new RuntimeException("Product not found: " + safeGet(row, 0)));

                    Discount discount = new Discount();
                    discount.setProduct(product);
                    discount.setStore(store);
                    discount.setFromDate(parseDateSafe(safeGet(row, 6)));
                    discount.setToDate(parseDateSafe(safeGet(row, 7)));
                    discount.setPercentage(parseDoubleSafe(safeGet(row, 8)));

                    discountRepository.save(discount);
                } catch (Exception e) {
                    log.error("Error processing discount row {}: {}", Arrays.toString(row), e.getMessage());
                }
            }
        }
    }

    private String safeGet(String[] row, int index) {
        return index < row.length ? cleanValue(row[index]) : "";
    }

    private String cleanValue(String value) {
        return value.replace(":", ";") // Fix common delimiter mistakes
                .replace("\"", "")  // Remove quotes
                .trim();
    }

    private Double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(cleanValue(value)
                    .replace(",", ".")  // Handle European decimal format
                    .replace("RON", "") // Remove currency symbols
                    .trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid number format: {}", value);
            return 0.0;
        }
    }

    private LocalDate parseDateSafe(String dateString) {
        try {
            return LocalDate.parse(cleanValue(dateString));
        } catch (DateTimeParseException e) {
            log.warn("Invalid date format: {}", dateString);
            return LocalDate.now();
        }
    }
}

