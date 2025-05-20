package com.accesa.price_comparator.loader;

import com.accesa.price_comparator.parser.CsvParser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final CsvParser csvParser;
    private final ResourceLoader resourceLoader;

    @Override
    public void run(String... args) throws Exception {
        loadStoreData("lidl", "2025-05-01");
        loadStoreData("lidl", "2025-05-08");

        loadStoreData("kaufland", "2025-05-01");
        loadStoreData("kaufland", "2025-05-08");

        loadStoreData("profi", "2025-05-01");
        loadStoreData("profi", "2025-05-08");
    }

    private void loadStoreData(String store, String date) throws Exception {
        LocalDate parsedDate = LocalDate.parse(date);
        InputStream prices = resourceLoader.getResource("classpath:data/" + store + "_" + date + ".csv").getInputStream();
        csvParser.parseProductCsv(prices, store, parsedDate);

        InputStream discounts = resourceLoader.getResource("classpath:data/" + store + "_discounts_" + date + ".csv").getInputStream();
        csvParser.parseDiscountCsv(discounts, store);
    }
}
