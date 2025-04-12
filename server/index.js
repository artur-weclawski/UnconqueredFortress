require('dotenv').config()


const express = require('express')
const app = express()
const cors = require('cors')
const connection = require('./db')
const registerRoutes = require("./routes/register")
const loginRoutes = require("./routes/login")
const downloadSavesRoutes = require("./routes/downloadSaves")
const uploadSaveRoutes = require("./routes/uploadSave")
const deleteSaveRoutes = require("./routes/deleteSave")
const pingRoutes = require("./routes/ping")

connection()
app.use(express.json())
app.use(cors())

app.use("/api/register", registerRoutes)
app.use("/api/login", loginRoutes)
app.use("/api/downloadSaves", downloadSavesRoutes)
app.use("/api/uploadSave", uploadSaveRoutes)
app.use("/api/deleteSave", deleteSaveRoutes)
app.use("/api/ping", pingRoutes)

const port = process.env.PORT || 9000
app.listen(port, () => console.log(`Nasłuchiwanie na porcie ${port}`))