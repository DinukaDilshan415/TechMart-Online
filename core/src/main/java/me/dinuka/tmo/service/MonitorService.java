package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import jakarta.enterprise.context.ApplicationScoped;
import me.dinuka.tmo.util.monitor.MetricsDTO;

@Remote
public interface MonitorService {
    MetricsDTO collect();
}
