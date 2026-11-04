package com.nutrigain.iam.domain;

import com.nutrigain.iam.domain.model.valueobjects.EmailAddress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailAddressTest {

    @Test
    void isNormalizedToLowerCase() {
        assertEquals("nico@example.com", new EmailAddress("  Nico@Example.COM ").value());
    }

    @Test
    void rejectsMalformedAddresses() {
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress("not-an-email"));
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress(null));
    }
}
