Setup:
After downloading the code, run the ParkingApplication.java class. 
When the startup is finished you can use the following commands to try out the functionalities:

Get Status:
curl -X GET http://localhost:8080/parking

Park vehicle:
curl -X POST http://localhost:8080/parking -H "Content-Type: application/json" -d "{\"vehicleReg\":\"ABC-123\",\"vehicleType\":1}"

Check out vehicle:
curl -X POST http://localhost:8080/parking/bill -H "Content-Type: application/json" -d "{\"vehicleReg\":\"ABC-123\"}"