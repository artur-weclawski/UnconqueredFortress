const router = require("express").Router()


router.post("/", async (req, res) => {
    try {
        return res.send({status: 200, message: "ResponseSuccessfulPing" })
        
    } catch (error) {
        res.send({status: 500, message: "ResponseInternalServerError" })
    }
})
module.exports = router