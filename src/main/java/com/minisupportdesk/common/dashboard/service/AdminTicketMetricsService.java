package com.minisupportdesk.common.dashboard.service;

import com.minisupportdesk.common.dashboard.DTO.TicketMetricsRespDTO;
import com.minisupportdesk.common.dashboard.DTO.TicketStats;
import com.minisupportdesk.entities.Role;
import com.minisupportdesk.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component()
@RequiredArgsConstructor
public class AdminTicketMetricsService implements MetricsService{

    private final StatsService statsService;

    @Override
    public TicketMetricsRespDTO getMetrics(User user) {

        TicketStats stats = statsService.getStats();

        return TicketMetricsRespDTO.builder()
                .todayTickets(stats.today())
                .weekTickets(stats.week())
                .openTickets(stats.open())
                .resolvedTickets(stats.resolved())
                .build();
    }

    @Override
    public Role supports() {
        return Role.ADMIN;
    }
}
