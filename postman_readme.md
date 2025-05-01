# Devices API Postman Collection

This repository contains a Postman collection and environment for testing the Devices API.

## Contents

- `postman_collection.json`: The Postman collection containing all API endpoints
- `postman_environment.json`: The Postman environment with variables used in the collection

## Importing into Postman

1. Open Postman
2. Click on "Import" in the top left corner
3. Drag and drop both files or click "Upload Files" and select both files
4. Click "Import"

## Using the Collection

1. After importing, select the "Devices API Environment" from the environment dropdown in the top right corner
2. The collection is organized into folders by endpoint type
3. You can modify the `baseUrl` variable in the environment settings if your API is running on a different host or port

## Available Endpoints

### Get Devices
- **Get All Devices**: Retrieves a collection of all devices
- **Get Device by ID**: Retrieves a device by its unique identifier
- **Get Devices by Brand**: Retrieves a collection of devices filtered by the specified brand
- **Get Devices by State**: Retrieves a collection of devices filtered by the specified state

### Create Device
- Creates a new device with the status AVAILABLE

### Update Device
- Updates the details of an existing device identified by its unique identifier

### Delete Device
- Deletes a device identified by its unique identifier

## Request Body Examples

### Create Device
```json
{
    "name": "iPhone 13",
    "brand": "Apple"
}
```

### Update Device
```json
{
    "name": "Updated Device Name",
    "brand": "Updated Brand",
    "state": "DISABLED"
}
```

## Device States
The following states are available for devices:
- AVAILABLE
- IN_USE
- DISABLED