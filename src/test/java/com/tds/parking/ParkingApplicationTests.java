package com.tds.parking;

import com.tds.parking.config.ParkingConfig;
import com.tds.parking.dto.*;
import com.tds.parking.exception.InvalidVehicleTypeException;
import com.tds.parking.exception.ParkingFullException;
import com.tds.parking.exception.VehicleNotFoundException;
import com.tds.parking.model.VehicleType;
import com.tds.parking.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParkingApplicationTests {

	private ParkingService service;

	@BeforeEach
	void setup() {
		service = new ParkingService();
	}

	@Test
	@DisplayName("Successful parking")
	void ParkingSuccessTest() {
		ParkVehicleRequest request = new ParkVehicleRequest("ABC-123", 1);
		ParkVehicleResponse response = service.parkVehicle(request);

		assertEquals("ABC-123", response.vehicleReg());
		assertEquals(1, response.spaceNumber());
		assertNotNull(response.timeIn());
	}

	@Test
	@DisplayName("Multiple successful parking")
	void ParkingMultipleSuccessTest() {
		service.parkVehicle(new ParkVehicleRequest("ABC-123", 1));
		service.parkVehicle(new ParkVehicleRequest("DEF-456", 2));

		ParkingStatusResponse status = service.getStatus();
		assertEquals(2, status.occupiedSpaces());
		assertEquals(8, status.availableSpaces());
	}

	@Test
	@DisplayName("Invalid vehicle type")
	void ParkingInvalidVehicleTypeTest() {
		ParkVehicleRequest request = new ParkVehicleRequest("ABC-123", 99);
		assertThrows(InvalidVehicleTypeException.class, () -> service.parkVehicle(request));
	}

	@Test
	@DisplayName("Full parking lot")
	void ParkingLotFullTest() {
		for (int i = 1; i <= ParkingConfig.TOTAL_SPACES; i++) {
			service.parkVehicle(new ParkVehicleRequest("ABC-" + i, 1));
		}

		assertThrows(ParkingFullException.class, () -> service.parkVehicle(new ParkVehicleRequest("ABC-Extra", 1)));
	}

	@Test
	@DisplayName("Calculate small vehicle")
	void BillingSmallTest() {
		OffsetDateTime timeIn = OffsetDateTime.now();
		OffsetDateTime timeOut = timeIn.plusMinutes(3);

		//0.10 * 3 = 0.30
		BigDecimal bill = service.calculateCharge(VehicleType.SMALL, timeIn, timeOut );
		assertEquals(new BigDecimal("0.30"), bill);
	}

	@Test
	@DisplayName("Calculate medium vehicle")
	void BillingMediumTest() {
		OffsetDateTime timeIn = OffsetDateTime.now();
		OffsetDateTime timeOut = timeIn.plusMinutes(3);

		//0.20 * 3 = 0.60
		BigDecimal bill = service.calculateCharge(VehicleType.MEDIUM, timeIn, timeOut );
		assertEquals(new BigDecimal("0.60"), bill);
	}

	@Test
	@DisplayName("Calculate large vehicle")
	void BillingLargeTest() {
		OffsetDateTime timeIn = OffsetDateTime.now();
		OffsetDateTime timeOut = timeIn.plusMinutes(3);

		//0.40 * 3 = 1.20
		BigDecimal bill = service.calculateCharge(VehicleType.LARGE, timeIn, timeOut );
		assertEquals(new BigDecimal("1.20"), bill);
	}

	@Test
	@DisplayName("Calculate large vehicle with 5 minute block")
	void ParkingLargeWithExtraTest() {
		OffsetDateTime timeIn = OffsetDateTime.now();
		OffsetDateTime timeOut = timeIn.plusMinutes(12);

		//0.40 * 12 + 2 (2 * 5 minutes) = 6.80
		BigDecimal bill = service.calculateCharge(VehicleType.LARGE, timeIn, timeOut );
		assertEquals(new BigDecimal("6.80"), bill);
	}

	@Test
	@DisplayName("Checkout vehicle")
	void ParkingCheckoutTest() {
		service.parkVehicle(new ParkVehicleRequest("ABC-123", 1));

		ExitVehicleRequest exitRequest = new ExitVehicleRequest("ABC-123");
		ExitVehicleResponse exitResponse = service.exitVehicle(exitRequest);

		assertEquals("ABC-123", exitResponse.vehicleReg());
		assertNotNull(exitResponse.billId());
		assertNotNull(exitResponse.vehicleCharge());

		assertEquals(10, service.getStatus().availableSpaces());
	}

	@Test
	@DisplayName("Exiting car not found")
	void ExitingCarNotFoundTest() {
		service.parkVehicle(new ParkVehicleRequest("ABC-123", 1));
		ExitVehicleRequest exitRequest = new ExitVehicleRequest("QQQ-999");

		assertThrows(VehicleNotFoundException.class, () -> service.exitVehicle(exitRequest));
	}
}
