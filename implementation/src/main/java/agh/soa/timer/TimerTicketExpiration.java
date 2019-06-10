package agh.soa.timer;

import agh.soa.model.Ticket;
import agh.soa.repository.TicketRepository;
import agh.soa.service.ITicketsService;
import lombok.Getter;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Singleton
@Startup
@Getter
public class TimerTicketExpiration {

    @Resource
    private SessionContext context;

    @EJB(lookup = "java:global/implementation-1.0-SNAPSHOT/TicketsService")
    ITicketsService ticketService;

    @Inject
    TicketRepository ticketRepository;


    public void createTimer(long duration) {
        context.getTimerService().createTimer(duration+1000, "ticketExpirationTimer");
    }

    @Timeout
    public void timeOutHandler(Timer allTimer) {

        System.out.println("Timeout of ticket for parking place with id: " + ticketService.getMostRecentTicket().getParkingPlace().getId());
        //ticketRepository.deleteTicketById(ticketService.getMostRecentTicket().getId());
        for (Timer timer : context.getTimerService().getAllTimers()) {
            if (timer.getInfo()=="ticketExpirationTimer") {
                timer.cancel();
            }
        }
        List<Ticket> allActiveTickets = ticketRepository.getAllActiveTickets();
        System.out.println("Size of all active tickets: "+allActiveTickets.size());

        if (allActiveTickets.size()==0) {
            return;
        }

        Ticket mostRecentTicket = allActiveTickets.get(0);
        ticketService.setMostRecentTicket(mostRecentTicket);
        long timeLeftInMillis = mostRecentTicket.getExpirationTime().getTime() - (new Date().getTime());
        createTimer(timeLeftInMillis);
    }
}
