package com.finance.tracker.service;

import com.finance.tracker.dto.RecurringTransactionDTO;
import com.finance.tracker.entity.RecurringTransaction;
import com.finance.tracker.mapper.RecurringTransactionMapper;
import com.finance.tracker.repository.RecurringTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecurringTransactionServiceTest {

    @InjectMocks
    private RecurringTransactionService recurringTransactionService;

    @Mock
    private RecurringTransactionRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        RecurringTransaction transaction = new RecurringTransaction();
        RecurringTransactionDTO dto = new RecurringTransactionDTO();

        when(repository.findAll()).thenReturn(List.of(transaction));

        try (MockedStatic<RecurringTransactionMapper> mocked = mockStatic(RecurringTransactionMapper.class)) {
            mocked.when(() -> RecurringTransactionMapper.toDto(transaction)).thenReturn(dto);

            List<RecurringTransactionDTO> result = recurringTransactionService.findAll();

            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
            verify(repository).findAll();
        }
    }

    @Test
    void testFindById_Found() {
        Long id = 1L;
        RecurringTransaction transaction = new RecurringTransaction();
        RecurringTransactionDTO dto = new RecurringTransactionDTO();

        when(repository.findById(id)).thenReturn(Optional.of(transaction));

        try (MockedStatic<RecurringTransactionMapper> mocked = mockStatic(RecurringTransactionMapper.class)) {
            mocked.when(() -> RecurringTransactionMapper.toDto(transaction)).thenReturn(dto);

            RecurringTransactionDTO result = recurringTransactionService.findById(id);

            assertNotNull(result);
            assertEquals(dto, result);
            verify(repository).findById(id);
        }
    }

    @Test
    void testFindById_NotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> recurringTransactionService.findById(id));
    }

    @Test
    void testFindByUserId() {
        Long userId = 1L;
        RecurringTransaction transaction = new RecurringTransaction();
        RecurringTransactionDTO dto = new RecurringTransactionDTO();

        when(repository.findByUserId(userId)).thenReturn(List.of(transaction));

        try (MockedStatic<RecurringTransactionMapper> mocked = mockStatic(RecurringTransactionMapper.class)) {
            mocked.when(() -> RecurringTransactionMapper.toDto(transaction)).thenReturn(dto);

            List<RecurringTransactionDTO> result = recurringTransactionService.findByUserId(userId);

            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
            verify(repository).findByUserId(userId);
        }
    }

    @Test
    void testSave() {
        RecurringTransaction transaction = new RecurringTransaction();
        RecurringTransactionDTO dto = new RecurringTransactionDTO();

        when(repository.save(transaction)).thenReturn(transaction);

        try (MockedStatic<RecurringTransactionMapper> mocked = mockStatic(RecurringTransactionMapper.class)) {
            mocked.when(() -> RecurringTransactionMapper.toDto(transaction)).thenReturn(dto);

            RecurringTransactionDTO result = recurringTransactionService.save(transaction);

            assertNotNull(result);
            assertEquals(dto, result);
            verify(repository).save(transaction);
        }
    }

    @Test
    void testDelete() {
        Long id = 1L;

        doNothing().when(repository).deleteById(id);

        recurringTransactionService.delete(id);

        verify(repository).deleteById(id);
    }
}
