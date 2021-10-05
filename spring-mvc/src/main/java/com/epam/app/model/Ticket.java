package com.epam.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by maksym_govorischev.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Ticket {
    public enum Category {STANDARD, PREMIUM, BAR}

    private long id;
    private long eventId;
    private long userId;
    private Category category;
    private int place;
}
