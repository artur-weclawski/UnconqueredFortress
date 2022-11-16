const router = require("express").Router()
const {User} = require("../models/user")
const bcrypt = require("bcrypt")

router.post("/", async (req, res) => {
    try {

        const dataFromRequest  = {login: req.headers.login, email: req.headers.mail, password: req.headers.password}
        const user1 = await User.findOne({ login: dataFromRequest.login })

        if (user1)
            return res.send({status: 409, message: "ResponseLoginTaken" })

        const user2 = await User.findOne({ email: dataFromRequest.email })
        
        if (user2)
            return res.send({status: 410, message: "ResponseMailTaken" })

        const salt = await bcrypt.genSalt(Number(process.env.SALT))
        const hashPassword = await bcrypt.hash(dataFromRequest.password, salt)

        await new User({ ...dataFromRequest, password: hashPassword }).save()
            
        res.send({status: 200, message: "ResponseUserCreated" })

    } catch (error) {
        res.send({status: 500, message: "ResponseInternalServerError" })
    }
})
module.exports = router