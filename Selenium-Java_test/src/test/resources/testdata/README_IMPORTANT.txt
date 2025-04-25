IMPORTANT: You need to create the following Excel files in this directory:

1. TestData.xlsx - With the following structure (as shown in README_TestData.md):
   - Sheet: "TestData"
   - Headers in row 1: TestCaseID, LoginURL, ValidUsername, ValidPassword, InvalidUsername, InvalidPassword, ErrorMessage, EmptyCredentialsError, DashboardTitle
   - Row 2 (data): LoginTest, https://www.example.com/login, validuser, validpass123, invaliduser, invalidpass, Invalid username or password, Please enter your credentials, Dashboard

2. XPathRepo.xlsx - With the following structure (as shown in README_XPathRepo.md):
   - Sheet: "LoginPage"
   - Column A: ElementName 
   - Column B: XPath
   - Row 2: usernameField, //input[@id='username' or @name='username']
   - Row 3: passwordField, //input[@id='password' or @name='password']
   - Row 4: loginButton, //button[@id='login-button' or contains(text(),'Login')]
   - Row 5: errorMessage, //div[@class='error-message' or @id='error-message']
   
   - Sheet: "DashboardPage"
   - Column A: ElementName
   - Column B: XPath
   - Row 2: welcomeMessage, //div[@id='welcome-message']
   - Row 3: logoutButton, //button[contains(text(),'Logout') or @id='logout']
   - Row 4: userProfileLink, //a[@id='user-profile' or contains(text(),'Profile')]

Once you've created these Excel files, you can run the tests. 