package com.gmail.onishchenko.oleksii.codenames.configuration;

import com.gmail.onishchenko.oleksii.codenames.entity.Room;
import com.gmail.onishchenko.oleksii.codenames.service.RoomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.Objects.isNull;

@Component
@Profile("!test")
public class ScheduledTasks {

    private static final Logger log = LogManager.getLogger(ScheduledTasks.class);

    private final RoomService roomService;

    @Value("${codenames.rooms.lifetime-minutes:30}")
    private int lifetime;

    @Autowired
    public ScheduledTasks(RoomService roomService) {
        log.info("Create instance of {}", ScheduledTasks.class.getName());
        this.roomService = roomService;
    }

    void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    @Scheduled(cron = "0 0,20,40 * * * *") //run every 20 minutes
    public void deleteOldRooms() {
        log.info("Deleting old rooms...");
        LocalDateTime validDate = LocalDateTime.now().minus(lifetime, ChronoUnit.MINUTES);
        List<Room> rooms = roomService.findAllAsRooms();
        int count = 0;
        for (int i = rooms.size() - 1; i >= 0; i--) {
            Room room = rooms.get(i);
            if (isNull(room.getDateModified()) || room.getDateModified().isBefore(validDate)) {
                roomService.deleteRoomById(room.getId());
                count++;
            }
        }
        log.info("{} old rooms were deleted", count);
    }
}
