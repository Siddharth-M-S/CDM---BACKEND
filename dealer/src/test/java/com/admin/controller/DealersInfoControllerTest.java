package com.admin.controller;

import com.admin.entity.DealerInfoEntity;
import com.admin.service.DealerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

 class DealersInfoControllerTest {

    @Mock
    private DealerService dealerService;

    @InjectMocks
    private DealersInfoController dealersInfoController;

    @Autowired
    private MockMvc mockMvc;

    private DealerInfoEntity dealerInfoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dealersInfoController).build();

        dealerInfoEntity = new DealerInfoEntity();
        dealerInfoEntity.setId(1L);
        dealerInfoEntity.setUsername("Dealer1");
        dealerInfoEntity.setEmail("dealer1@example.com");
        dealerInfoEntity.setPhoneNumber(Long.valueOf("1234567890"));
    }

    @Test
    void testViewDealer() throws Exception {
        List<DealerInfoEntity> dealers = Arrays.asList(dealerInfoEntity);
        when(dealerService.getAllDealers()).thenReturn(dealers);

        mockMvc.perform(get("/api/admin/dealer/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].username").value("Dealer1"));
    }

    @Test
    void testDealerById_Success() throws Exception {
        when(dealerService.dealerById(1L)).thenReturn(Optional.of(dealerInfoEntity));

        mockMvc.perform(get("/api/admin/dealer/view/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Dealer1"));
    }
//

    @Test
    void testDeleteDealer() throws Exception {
        when(dealerService.deleteDealer(1L)).thenReturn("Dealer deleted successfully");

        mockMvc.perform(delete("/api/admin/dealer/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dealer deleted successfully"));
    }

    @Test
    void testUpdateDealer() throws Exception {
        DealerInfoEntity updatedDealer = new DealerInfoEntity();
        updatedDealer.setId(1L);
        updatedDealer.setUsername("UpdatedDealer");
        updatedDealer.setEmail("updated@example.com");
        updatedDealer.setPhoneNumber(Long.valueOf("9876543210"));

        when(dealerService.updateDealer(eq(1L), any(DealerInfoEntity.class))).thenReturn(updatedDealer);

        mockMvc.perform(put("/api/admin/dealer/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"UpdatedDealer\",\"email\":\"updated@example.com\",\"phoneNumber\":\"9876543210\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("UpdatedDealer"));
    }

    @Test
    void testGetMaxDealerPurchases() throws Exception {
        List<Map<String, Object>> maxPurchases = Arrays.asList(
                Map.of("dealerId", 1L, "purchases", 100)
        );
        when(dealerService.getMaximumDealerPurchases()).thenReturn(maxPurchases);

        mockMvc.perform(get("/api/admin/dealer/maxPurchases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].dealerId").value(1L))
                .andExpect(jsonPath("$[0].purchases").value(100));
    }
}

