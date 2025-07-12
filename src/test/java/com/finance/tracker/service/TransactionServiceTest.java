package com.finance.tracker.service;

import com.finance.tracker.dto.*;
import com.finance.tracker.entity.*;
import com.finance.tracker.mapper.TransactionMapper;
import com.finance.tracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTransaction() {
        User user = new User();
        TransactionDTO dto = new TransactionDTO();
        Transaction entity = new Transaction();

        when(transactionMapper.toTransactionEntity(dto, user)).thenReturn(entity);
        when(transactionRepository.save(entity)).thenReturn(entity);
        when(transactionMapper.toTransactionDTO(entity)).thenReturn(dto);

        TransactionDTO result = transactionService.saveTransaction(dto, user);

        assertEquals(dto, result);
        verify(transactionRepository).save(entity);
    }

    @Test
    void testEditTransaction_Success() {
        Long id = 1L;
        User user = new User();
        user.setId(2L);
        Transaction existing = new Transaction();
        existing.setId(id);
        existing.setUser(user);
        TransactionDTO updatedDto = new TransactionDTO();
        updatedDto.setAmount(BigDecimal.valueOf(100));

        when(transactionRepository.findByIdAndUserId(id, user.getId())).thenReturn(Optional.of(existing));
        when(transactionMapper.toCategoryEntity(updatedDto.getCategory())).thenReturn(new Category());
        when(transactionRepository.save(existing)).thenReturn(existing);
        when(transactionMapper.toTransactionDTO(existing)).thenReturn(updatedDto);

        TransactionDTO result = transactionService.editTransaction(id, updatedDto, user);

        assertEquals(updatedDto, result);
        verify(transactionRepository).save(existing);
    }

    @Test
    void testEditTransaction_NotFound() {
        when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> transactionService.editTransaction(1L, new TransactionDTO(), new User()));
    }

    @Test
    void testDeleteTransaction_Success() {
        User user = new User();
        user.setId(1L);
        Transaction transaction = new Transaction();

        when(transactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(1L, user);

        verify(transactionRepository).delete(transaction);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> transactionService.deleteTransaction(1L, new User()));
    }

    @Test
    void testGetAllTransactions() {
        Transaction transaction = new Transaction();
        TransactionDTO dto = new TransactionDTO();

        when(transactionRepository.findAll()).thenReturn(List.of(transaction));
        when(transactionMapper.toTransactionDTO(transaction)).thenReturn(dto);

        List<TransactionDTO> result = transactionService.getAllTransactions();

        assertEquals(1, result.size());
        verify(transactionRepository).findAll();
    }

    @Test
    void testGetAllTransactionsByUser() {
        Long userId = 1L;
        Transaction transaction = new Transaction();
        TransactionDTO dto = new TransactionDTO();

        when(transactionRepository.findByUserId(userId)).thenReturn(List.of(transaction));
        when(transactionMapper.toTransactionDTO(transaction)).thenReturn(dto);

        List<TransactionDTO> result = transactionService.getAllTransactionsByUser(userId);

        assertEquals(1, result.size());
    }

    @Test
    void testGetTransactionSummary() {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now().minusDays(30);
        LocalDateTime end = LocalDateTime.now();

        Transaction tx1 = new Transaction();
        tx1.setAmount(BigDecimal.valueOf(200));
        tx1.setType(TransactionType.income);
        tx1.setCategory(Category.builder().name("Salary").build());

        Transaction tx2 = new Transaction();
        tx2.setAmount(BigDecimal.valueOf(50));
        tx2.setType(TransactionType.expense);
        tx2.setCategory(Category.builder().name("Food").build());

        when(transactionRepository.findByUserIdAndCreatedAtBetween(userId, start, end))
                .thenReturn(List.of(tx1, tx2));

        TransactionSummaryResponse response = transactionService.getTransactionSummary(userId, start, end);

        assertEquals(200, response.getTotalIncome());
        assertEquals(50, response.getTotalExpense());
        assertEquals(150, response.getNetSavings());
        assertTrue(response.getCategoryWiseBreakdown().containsKey("Food"));
    }

    @Test
    void testGetMonthlyCategorySummary() {
        Long userId = 1L;
        YearMonth month = YearMonth.now();
        CategoryMonthlySummaryDTO dto = new CategoryMonthlySummaryDTO("Salary", new BigDecimal("1000"));

        when(transactionRepository.getCategorySummaryByMonth(userId, month.getMonthValue(), month.getYear()))
                .thenReturn(List.of(dto));

        List<CategoryMonthlySummaryDTO> result = transactionService.getMonthlyCategorySummary(userId, month);

        assertEquals(1, result.size());
    }

    @Test
    void testGetFilteredTransactions() {
        Long userId = 1L;
        Transaction transaction = new Transaction();
        TransactionDTO dto = new TransactionDTO();
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction), PageRequest.of(0, 10), 1);


        when(transactionRepository.findAll(
                ArgumentMatchers.<Specification<Transaction>>any(),
                any(Pageable.class)))
                .thenReturn(transactionPage);
        when(transactionMapper.toTransactionDTO(transaction)).thenReturn(dto);

        PagedResponse<TransactionDTO> response = transactionService.getFilteredTransactions(
                userId, null, null, null, null,
                LocalDate.now().minusDays(30), LocalDate.now(),
                0, 10, "createdAt", "desc"
        );

        assertEquals(1, response.getContent().size());
    }
}
