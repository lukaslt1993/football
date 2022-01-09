## Description
REST API with CRUD operations to manipulate football team and player data

On top of the standard operations, I introduced a functionality to transfer a player to another team. Transfer means that one team buys a player from another team.
Player contract is calculated like this:
- Transfer Fee = Months of experience * 100,000 / Age
- Contract Fee = Transfer Fee + Team Commission
- Team Commission will be up to 10% of the Transfer Fee
- Each team will calculate the Contract Fee in its own currency


## Usage
Start currency app and then start football app

## Swagger
localhost:8080/swagger-ui/ (note the last slash)
