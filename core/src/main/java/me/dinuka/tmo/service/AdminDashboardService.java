package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.entity.Admin;

@Remote
public interface AdminDashboardService {
    String getDashboardData(Admin admin);
    String getOrdersData(Admin admin);
    String getUsersData(Admin admin);
    String getCustomersData(Admin admin);
}
