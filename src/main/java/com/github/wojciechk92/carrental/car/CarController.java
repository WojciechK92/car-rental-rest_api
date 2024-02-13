package com.github.wojciechk92.carrental.car;

import com.github.wojciechk92.carrental.car.dto.CarReadModel;
import com.github.wojciechk92.carrental.car.dto.CarWriteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("cars")
public class CarController {
  private final CarService carService;

  @Autowired
  public CarController(CarService carService) {
    this.carService = carService;
  }

  @GetMapping
  public ResponseEntity<List<CarReadModel>> getCars(Pageable pageable) {
    return ResponseEntity.ok(carService.getCars(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CarReadModel> getCar(@PathVariable Long id) {
    return ResponseEntity.ok(carService.getCar(id));
  }

  @PostMapping
  public ResponseEntity<CarReadModel> createCar(@RequestBody CarWriteModel car) {
    CarReadModel result = carService.createCar(car);
    return ResponseEntity.created(URI.create("/cars/" + result.getId())).body(result);
  }
}