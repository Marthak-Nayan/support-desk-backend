package com.minisupportdesk.common.dashboard.service;

import com.minisupportdesk.Repository.TicketRepositary;
import com.minisupportdesk.common.dashboard.DTO.TicketMetricsRespDTO;
import com.minisupportdesk.entities.Role;
import com.minisupportdesk.entities.Status;
import com.minisupportdesk.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component()
@RequiredArgsConstructor
public class UserAdminMetricsService implements MetricsService{

    private final TicketRepositary ticketRepositary;

    @Override
    public TicketMetricsRespDTO getMetrics(User user) {
        LocalDate todayDate = LocalDate.now();

        //Today Time range
        LocalDateTime startOfDay = todayDate.atStartOfDay();
        LocalDateTime endOfDay = todayDate.plusDays(1).atStartOfDay();

        //Week range
        LocalDate startOfWeek = todayDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = todayDate.with(DayOfWeek.SUNDAY);


        LocalDateTime startOfWeekDT = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDT = endOfWeek.plusDays(1).atStartOfDay();

        long todayCount = ticketRepositary.countByCreatedByAndCreatedAtBetween(user,startOfDay, endOfDay);
        long weekCount = ticketRepositary.countByCreatedByAndCreatedAtBetween(user, startOfWeekDT,endOfWeekDT);
        long openCount = ticketRepositary.countByCreatedByAndStatus(user, Status.OPEN);
        long resolveCount = ticketRepositary.countByCreatedByAndStatus(user,Status.RESOLVED);

        return TicketMetricsRespDTO.builder()
                .openTickets(openCount)
                .resolvedTickets(resolveCount)
                .weekTickets(weekCount)
                .todayTickets(todayCount)
                .build();
    }

    @Override
    public Role supports() {
        return Role.USER;
    }
}
