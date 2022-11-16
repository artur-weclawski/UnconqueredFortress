const router = require("express").Router()
const { User } = require("../models/user")
const bcrypt = require("bcrypt")

router.post("/", async (req, res) => {
    try {
        
        const dataFromRequest  = {login: req.headers.login, password: req.headers.password}
        const user = await User.findOne({ login: dataFromRequest.login })
        
        if (!user)
            return res.send({ status: 400, message: "ResponseNoUserWithThisLogin" })
        
        const validPassword = await bcrypt.compare(dataFromRequest.password, user.password)
        
        if (!validPassword)
            return res.send({ status: 401, message: "ResponseWrongPassword" })

        const token = user.generateAuthToken();
        res.send({ status: 200, token: token, message: "ResponseLoggedSuccessfully" })

    } catch (error) {
        res.status(500).send({ status: 500, message: "ResponseInternalServerError" })
    }
})

module.exports = router