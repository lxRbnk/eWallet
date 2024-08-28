package com.wallet.report.service;

import com.wallet.report.dto.TransactionDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public ByteArrayInputStream createExcelFile(List<TransactionDto> transactions) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet("Transactions");
            String[] columns = {"Type", "Wallet Sender", "Wallet Receiver", "Amount", "Currency", "Currency To", "Timestamp", "Status"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowIdx = 1;
            for (TransactionDto transaction : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(transaction.getType());
                row.createCell(1).setCellValue(transaction.getWalletSender().toString());
                row.createCell(2).setCellValue(transaction.getWalletReceiver().toString());
                row.createCell(3).setCellValue(transaction.getAmount().toString());
                row.createCell(4).setCellValue(transaction.getCurrency());
                row.createCell(5).setCellValue(transaction.getCurrencyTo() != null ? transaction.getCurrencyTo() : "");
                row.createCell(6).setCellValue(transaction.getTimeStamp().toString());
                row.createCell(7).setCellValue(transaction.isStatus() ? "Completed" : "Pending");
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } finally {
            workbook.close();
        }
    }
}
