# Step by Step

### `Command Prompt / Powershell`

Start your Strapi V4 application by typing this in the terminal : 

```
npx create-strapi-app@latest yourprojectname
# or
yarn create strapi-app yourprojectname
```

### `build`

After the project successfuly created, build your admin panel. Run this command :

```
npm run build
# or
yarn run build
```

### `start`

Start the server with this command : 

```
npm run develop
# or
yarn develop
```

### `Open admin panel`

Open this link : http://localhost:1337/admin to open Strapi dashboard and make your API of your choice

## ⚙️ Deployment

We use Cloud Run to deploy the API . Here is the steps : 

### `Install Cloud SDK`

Install Cloud SDK in your computer 

### `GCLOUD INIT`

Enter the directory of the project and run it's terminal with VSCode or CMD. And then run this command 

```
gcloud init
```

### `Region`

Select the region of your choice

```
example : us-central-1
```

### `Authenticate the Project ID and your account`

Run these commands to set the project and account as a default :

```
gcloud config set project yourproject
gcloud config set account yourregisteredaccount
```

### `Cloud SQL`

Enter Google Cloud Console and create a New Instance with MySQL :

```
Enter the instance name
Create password (I prefer not to set password)
Use the latest MySQL version
Then click "Create Instance"
```

After the instance has been created, copy the Public IP address in the "Overview" section, and then paste it in .env in your project as HOST and copy the instance name as well and the paste it as INSTANCE_CONNECTION_NAME.

Create database :

```
Go to database section
Create a new database
Enter database name
Create database
```

After the database has been created, copy the database name in the "Database" section, and then paste it in .env in your project as DATABASE_NAME.

Open the Users section, copy the Username and then paste it in .env in your project as DATABASE_USER.

Leave the DATABASE_PASSWORD empty if you dont set the password earlier.

Back to the terminal , then run this command : 

```
gcloud run deploy
```

### `Cloud Run`

Wait after view minutes and back to Google Cloud Console. Open Ckoud Run and then click the URL it produced. 

### `Congratulations ! You got your endpoint ready. See the complete documentation at https://docs.strapi.io/developer-docs/latest`
