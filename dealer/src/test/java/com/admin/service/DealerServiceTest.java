package com.admin.service;

import com.admin.entity.DealerInfoEntity;
import com.admin.repository.DealerInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealerServiceTest {
    @Mock
    private DealerInfoRepository repo;

    @InjectMocks
    private DealerService dealerService;

    private DealerInfoEntity dealer1;
    private DealerInfoEntity dealer2;

    @BeforeEach
    void setUp() {
        dealer1 = new DealerInfoEntity();
        dealer1.setId(1L);
        dealer1.setUsername("dealer1");
        dealer1.setEmail("dealer1@example.com");

        dealer2 = new DealerInfoEntity();
        dealer2.setId(2L);
        dealer2.setUsername("dealer2");
        dealer2.setEmail("dealer2@example.com");
    }
    @Test
    void getAllDealers() {
        when(repo.findAll()).thenReturn(Arrays.asList(dealer1, dealer2));

        List<DealerInfoEntity> dealers = dealerService.getAllDealers();

        assertNotNull(dealers);
        assertEquals(2, dealers.size());
        verify(repo, times(1)).findAll();
    }
    @Test
    void getAllDealers_emptyList() {

        when(repo.findAll()).thenReturn(Arrays.asList());


        List<DealerInfoEntity> dealers = dealerService.getAllDealers();


        assertNotNull(dealers);
        assertEquals(0, dealers.size());

        verify(repo, times(1)).findAll();
    }

    @Test
    void saveDealer() {
        when(repo.save(any(DealerInfoEntity.class))).thenReturn(dealer1);

        String result = dealerService.saveDealer(dealer1);

        assertEquals("Dealer added successfully", result);
        verify(repo, times(1)).save(dealer1);
    }
    @Test
    void saveDealer_failure() {
        when(repo.save(any(DealerInfoEntity.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> dealerService.saveDealer(dealer1));

        verify(repo, times(1)).save(dealer1);
    }




    @Test
    void updateDealer() {
        when(repo.findById(1L)).thenReturn(Optional.of(dealer1));
        when(repo.save(any(DealerInfoEntity.class))).thenReturn(dealer1);

        DealerInfoEntity updatedDealer = new DealerInfoEntity();
        updatedDealer.setUsername("UpdatedDealer");
        updatedDealer.setEmail("updated@example.com");

        DealerInfoEntity result = dealerService.updateDealer(1L, updatedDealer);

        assertNotNull(result);
        assertEquals("UpdatedDealer", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).save(dealer1);
    }
}