# XPath Repository Excel Structure

The `XPathRepo.xlsx` file contains all XPath expressions for elements used in the automation framework. Each page should have its own sheet in the Excel file.

## Sheet: LoginPage

| ElementName   | XPath                                                 |
|---------------|-------------------------------------------------------|
| usernameField | //input[@id='username' or @name='username']           |
| passwordField | //input[@id='password' or @name='password']           |
| loginButton   | //button[@id='login-button' or contains(text(),'Login')] |
| errorMessage  | //div[@class='error-message' or @id='error-message']  |

## Sheet: DashboardPage

| ElementName      | XPath                                              |
|------------------|--------------------------------------------------- |
| welcomeMessage   | //div[@id='welcome-message']                       |
| logoutButton     | //button[contains(text(),'Logout') or @id='logout']|
| userProfileLink  | //a[@id='user-profile' or contains(text(),'Profile')] |

## Sheet: MyntraPage

| ElementName      | XPath                                              |
|------------------|--------------------------------------------------- |
| searchBox        | //input[@class='desktop-searchBar' or @placeholder='Search for products, brands and more'] |
| productItem      | (//li[contains(@class, 'product-base')])[{index}]  |
| sizeOption       | //button[contains(@class, 'size-buttons-size-button') or contains(@class, 'size-variant-button')][1] |
| addToCartButton  | //div[contains(@class,'pdp-add-to-bag') or @class='btn-gold' or contains(text(),'ADD TO BAG')] |
| cartIcon         | //span[contains(@class,'myntraweb-sprite desktop-iconBag')] |
| cartItem         | //div[contains(@class,'itemContainer')] |

## Guidelines:

1. Each sheet should represent a page or component
2. The first column should be ElementName (descriptive name of the UI element)
3. The second column should be the XPath expression
4. Use relative and robust XPath expressions
5. Avoid using indexes in XPath where possible
6. Include multiple attribute checks for more robust element location 