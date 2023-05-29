package com.wuav.client.gui.entities;

import com.wuav.client.be.Customer;

import java.util.List;

/**
 * The record Dashboard data.
 */
public record DashboardData(
     int totalProjects,
     int totalDeviceUser,
     List<Customer> recentCustomers,
     int amountOfPlansUploaded
){}
