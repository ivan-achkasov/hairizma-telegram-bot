package com.hairizma.service;

import com.hairizma.dto.CustomerDeliveryInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    final Map<Long, CustomerDeliveryInfo> deliveriesInfo = new HashMap<>();

    @Override
    public CustomerDeliveryInfo getCustomerInfo(final long chatId) {
        synchronized (Long.valueOf(chatId)) {
            return deliveriesInfo.get(chatId);
        }
    }

    @Override
    public CustomerDeliveryInfo getOrCreateCustomerInfo(long chatId) {
        synchronized (Long.valueOf(chatId)) {
            CustomerDeliveryInfo deliveryInfo = getCustomerInfo(chatId);
            if (deliveryInfo == null) {
                deliveryInfo = new CustomerDeliveryInfo(chatId);
            }
            return deliveryInfo;
        }
    }

    @Override
    public void saveCustomerInfo(final CustomerDeliveryInfo customerDeliveryInfo) {
        if (customerDeliveryInfo == null) {
            return;
        }
        synchronized (Long.valueOf(customerDeliveryInfo.getId())) {
            deliveriesInfo.put(customerDeliveryInfo.getId(), customerDeliveryInfo);
        }
    }

}
