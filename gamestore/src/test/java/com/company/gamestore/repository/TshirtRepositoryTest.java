package com.company.gamestore.repository;

import com.company.gamestore.model.Tshirt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TshirtRepositoryTest {

    @Autowired
    TshirtRepository tshirtRepo;

    private Tshirt tshirt;

    @BeforeEach
    public void setUp(){
        tshirtRepo.deleteAll();

        // Arrange
        tshirt = new Tshirt();
        tshirt.setSize("Test Size");
        tshirt.setColor("Test Color");
        tshirt.setDescription("Test Description");
        tshirt.setPrice(1.00);
        tshirt.setQuantity(1);
        tshirt = tshirtRepo.save(tshirt);
    }

    @Test
    public void addTshirt(){
        // Assert
        Optional<Tshirt> foundTshirt = tshirtRepo.findById(tshirt.getTshirtId());
        assertEquals(foundTshirt.get(), tshirt);
    }

    @Test
    public void updateTshirt(){
        // Act
        tshirt.setSize("UPDATED SIZE");
        tshirtRepo.save(tshirt);

        // Assert
        Optional<Tshirt> foundTshirt = tshirtRepo.findById(tshirt.getTshirtId());
        assertEquals(foundTshirt.get(), tshirt);
    }

    @Test
    public void deleteTshirt(){
        // Act
        tshirtRepo.deleteById(tshirt.getTshirtId());

        // Assert
        Optional<Tshirt> foundTshirt = tshirtRepo.findById(tshirt.getTshirtId());
        assertFalse(foundTshirt.isPresent());
    }

    @Test
    public void getTshirtById(){
        // Act
        Tshirt foundTshirt = tshirtRepo.findById(tshirt.getTshirtId()).orElse(null);

        // Assert
        assertEquals(tshirt, foundTshirt);
    }

    @Test
    public void getTshirtByColor(){
        // Arrange
        Tshirt tshirt2 = new Tshirt();
        tshirt2.setSize("Test Size 2");
        tshirt2.setColor("Test Color 2");
        tshirt2.setDescription("Test Description 2");
        tshirt2.setPrice(2.00);
        tshirt2.setQuantity(2);
        tshirtRepo.save(tshirt2);

        // Act
        List<Tshirt> tshirts = tshirtRepo.findByColor(tshirt.getColor());

        // Assert
        assertTrue(tshirts.contains(tshirt));
        assertFalse(tshirts.contains(tshirt2));
    }

    @Test
    public void getTshirtBySize(){
        // Arrange
        Tshirt tshirt2 = new Tshirt();
        tshirt2.setSize("Test Size 2");
        tshirt2.setColor("Test Color 2");
        tshirt2.setDescription("Test Description 2");
        tshirt2.setPrice(2.00);
        tshirt2.setQuantity(2);
        tshirtRepo.save(tshirt2);

        // Act
        List<Tshirt> tshirts = tshirtRepo.findBySize(tshirt.getSize());

        // Assert
        assertTrue(tshirts.contains(tshirt));
        assertFalse(tshirts.contains(tshirt2));
    }
}
