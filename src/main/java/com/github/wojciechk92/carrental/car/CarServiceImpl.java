package com.github.wojciechk92.carrental.car;

import com.github.wojciechk92.carrental.car.dto.CarReadModel;
import com.github.wojciechk92.carrental.car.dto.CarWriteModel;
import com.github.wojciechk92.carrental.car.exception.CarException;
import com.github.wojciechk92.carrental.car.exception.CarExceptionMessage;
import com.github.wojciechk92.carrental.car.validator.CarValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CarServiceImpl implements CarService {
  private final CarRepository carRepository;
  private final CarValidator carValidator;

  @Autowired
  CarServiceImpl(CarRepository carRepository, CarValidator carValidator) {
    this.carRepository = carRepository;
    this.carValidator = carValidator;
  }

  @Override
  public List<CarReadModel> getCars(CarStatus status, Pageable pageable) {
    if (status == null) {
      return carRepository.findAll(pageable).getContent().stream()
              .map(CarReadModel::new)
              .toList();
    }

    return carRepository.findAllByStatus(status, pageable).getContent().stream()
            .map(CarReadModel::new)
            .toList();
  }

  @Override
  public List<CarReadModel> getCarsByIdList(List<Long> list) {
    List<CarReadModel> result = carRepository.findAllByIdIsIn(list).stream()
            .map(CarReadModel::new)
            .toList();
    if (list.size() != result.size()) throw new CarException(CarExceptionMessage.LIST_CONTAINS_WRONG_CAR_ID);
    return result;
  }

  @Override
  public CarReadModel getCar(Long id) {
    return carRepository.findById(id)
            .map(CarReadModel::new)
            .orElseThrow(() -> new CarException(CarExceptionMessage.CAR_NOT_FOUND));
  }

  @Override
  public CarReadModel createCar(CarWriteModel toSave) {
    carValidator.validateCarYear(toSave.getProductionYear());
    Car result = carRepository.save(toSave.toCar());
    return new CarReadModel(result);
  }


  @Transactional
  @Override
  public void updateCar(CarWriteModel toUpdate, Long id) {
    carValidator.validateCarYear(toUpdate.getProductionYear());

    carRepository.findById(id)
            .map(car -> {
              car.setMake(toUpdate.getMake());
              car.setModel(toUpdate.getModel());
              car.setProductionYear(toUpdate.getProductionYear());
              car.setPricePerDay(toUpdate.getPricePerDay());
              car.setStatus(toUpdate.getStatus());
              return car;
            })
            .orElseThrow(() -> new CarException(CarExceptionMessage.CAR_NOT_FOUND));
  }

  @Transactional
  @Override
  public void setStatusTo(CarStatus status, Long id) {
    carRepository.findById(id)
            .map(car -> {
              car.setStatus(status);
              return car;
            })
            .orElseThrow(() -> new CarException(CarExceptionMessage.CAR_NOT_FOUND));
  }

  @Transactional
  @Override
  public void setStatusForCarsFromIdList(List<Long> list, CarStatus status) {
    List<Car> cars = carRepository.findAllByIdIsIn(list);
    cars.forEach(car -> car.setStatus(status));
  }
}
