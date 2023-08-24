package com.company.gamestore.service;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Tshirt;
import com.company.gamestore.repository.TshirtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TshirtService {
    private final TshirtRepository tshirtRepository;

    @Autowired
    public TshirtService(TshirtRepository tshirtRepository){
        this.tshirtRepository = tshirtRepository;
    }

    public Tshirt addTshirt(Tshirt tshirt) {
        if (tshirt.getSize() == null || tshirt.getColor() == null || tshirt.getDescription() == null || tshirt.getPrice() == null || tshirt.getQuantity() == 0){
            throw new InvalidException("All tshirt fields must be filled.");
        }
        if (tshirt.getPrice() < 0){
            throw new InvalidException("Tshirt price must be greater than 0.");
        }

        return tshirtRepository.save(tshirt);
    }

    public List<Tshirt> getAllTshirts() {
        List<Tshirt> tshirts = tshirtRepository.findAll();
        if (tshirts == null || tshirts.isEmpty()) {
            throw new NotFoundException("No tshirts found.");
        }
        return tshirts;
    }

    public Optional<Tshirt> getTshirtById(int id) {
        if (!tshirtRepository.existsById(id)) {
            throw new NotFoundException("Tshirt not found with id: " + id);
        }
        return tshirtRepository.findById(id);
    }

    public List<Tshirt> findByColor(String color) {
        List<Tshirt> tshirtsByColor = tshirtRepository.findByColor(color);
        if (tshirtsByColor == null || tshirtsByColor.isEmpty()) {
            throw new NotFoundException("No tshirts found with color: " + color);
        }
        return tshirtsByColor;
    }

    public List<Tshirt> findBySize(String size) {
        List<Tshirt> tshirtsBySize = tshirtRepository.findBySize(size);
        if (tshirtsBySize == null || tshirtsBySize.isEmpty()) {
            throw new NotFoundException("No tshirts found with size: " + size);
        }
        return tshirtsBySize;
    }

    public Tshirt updateTshirt(Tshirt tshirt) {
        if (!tshirtRepository.existsById(tshirt.getTshirtId())) {
            throw new NotFoundException("Tshirt not found with id: " + tshirt.getTshirtId());
        }
        if (tshirt.getSize() == null || tshirt.getColor() == null || tshirt.getDescription() == null || tshirt.getPrice() == null || tshirt.getQuantity() == 0){
            throw new InvalidException("All tshirt fields must be filled.");
        }
        if (tshirt.getPrice() < 0){
            throw new InvalidException("Tshirt price must be greater than 0.");
        }

        return tshirtRepository.save(tshirt);
    }

    public void deleteTshirt(int id) {
        if (!tshirtRepository.existsById(id)) {
            throw new NotFoundException("Tshirt not found with id: " + id);
        }
        tshirtRepository.deleteById(id);
    }
}

