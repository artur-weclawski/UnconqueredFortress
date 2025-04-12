const router = require("express").Router()
const { Save } = require("../models/save")
const jwt = require("jsonwebtoken")

router.post("/", async (req, res) => {
    try {

        const dataFromRequest  = {login: req.headers.login , profileNumber: req.headers.profilenumber, token: req.headers.token}

        if (dataFromRequest.token=="")
            return res.send({status: 401, message: "ResponseUnauthorized"})
        if (!jwt.verify( dataFromRequest.token, process.env.JWTPRIVATEKEY))
            return res.send({status: 401, message: "ResponseUnauthorized"})

        const save = await Save.exists({ login: dataFromRequest.login, profileNumber: dataFromRequest.profileNumber });
        
        if (!save)
            return res.send({status: 403, message: "ResponseSaveDoesntExist" })

        await Save.findOneAndDelete({ login: dataFromRequest.login, profileNumber: dataFromRequest.profileNumber })
            return res.send({status: 200, message: "ResponseSuccessfullyDeletedSave" })

    } catch (error) {
        if (error instanceof jwt.JsonWebTokenError)
            return res.send({status: 401, message: "ResponseUnauthorized"})
        res.send({status: 500, message: "ResponseInternalServerError" })
    }
})
module.exports = router