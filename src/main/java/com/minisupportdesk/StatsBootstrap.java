package com.minisupportdesk;

import com.minisupportdesk.Repository.TicketRepositary;
import com.minisupportdesk.common.dashboard.service.StatsService;
import com.minisupportdesk.entities.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatsBootstrap {

    private final TicketRepositary ticketRepositary;
    private final StatsService statsService;
    private final RedisConnectionFactory redisConnectionFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        log.info("Rebuilding ticket dashboard cache...");

        LocalDate todayDate = LocalDate.now();

        //Today Time range
        LocalDateTime startOfDay = todayDate.atStartOfDay();
        LocalDateTime endOfDay = todayDate.plusDays(1).atStartOfDay();

        //Week range
        LocalDate startOfWeek = todayDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = todayDate.with(DayOfWeek.SUNDAY);


        LocalDateTime startOfWeekDT = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDT = endOfWeek.plusDays(1).atStartOfDay();

        statsService.resetAll();

        statsService.set("tickets:open",ticketRepositary.countByStatus(Status.OPEN));
        statsService.set("tickets:week",ticketRepositary.countByCreatedAtBetween(startOfWeekDT,endOfWeekDT));
        statsService.set("tickets:today",ticketRepositary.countByCreatedAtBetween(startOfDay, endOfDay));
        statsService.set("tickets:resolved",ticketRepositary.countByStatus(Status.RESOLVED));

        log.info("Ticket dashboard cache initialized successfully");

    }

}
