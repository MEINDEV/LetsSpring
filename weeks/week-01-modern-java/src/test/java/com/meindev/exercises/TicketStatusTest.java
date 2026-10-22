package com.meindev.exercises;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketStatusTest {
    @Test
    public void openCanMoveToClosed(){
        assertThat(TicketStatus.OPEN.canMoveTo(TicketStatus.CLOSED)).isTrue();
    }

    @Test
    public void openCanMoveToInProgress(){
        assertThat(TicketStatus.OPEN.canMoveTo(TicketStatus.IN_PROGRESS)).isTrue();
    }

    @Test
    public void InProgressCanMoveToResolved(){
        assertThat(TicketStatus.IN_PROGRESS.canMoveTo(TicketStatus.RESOLVED)).isTrue();
    }

    @Test
    public void resolvedCanMoveToInProgress(){
        assertThat(TicketStatus.RESOLVED.canMoveTo(TicketStatus.CLOSED )).isTrue();
    }

    @Test void closedStatusIsFinalStatus(){
        assertThat(TicketStatus.CLOSED.isFinal()).isTrue();
    }

    @Test void closedCanNotBeResolved(){
        assertThat(TicketStatus.CLOSED.canMoveTo(TicketStatus.RESOLVED)).isFalse();
    }

    @Test void resolveCanNotMoveToOpen(){
        assertThat(TicketStatus.RESOLVED.canMoveTo(TicketStatus.OPEN)).isFalse();
    }

    @Test void InProgressCanNotBeOpen(){
        assertThat(TicketStatus.IN_PROGRESS.canMoveTo(TicketStatus.OPEN)).isFalse();
    }

}
