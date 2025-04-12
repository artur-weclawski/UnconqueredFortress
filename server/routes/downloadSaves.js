const router = require("express").Router()
const { Save } = require("../models/save")


router.post("/", async (req, res) => {
    try {

        const dataFromRequest  = {login: req.headers.login }
        const save = await Save.exists({ login: dataFromRequest.login});

        if (save)
        {
            const saves = await Save.find({ login: dataFromRequest.login })
            return res.send({status: 200, message: "ResponseSuccessfullyLoadedSaves", loadedData: saves })
        }
        else 
            return res.send({status: 201, message: "ResponseNoExistingSaves" })
    } catch (error) {
        res.send({status: 500, message: "ResponseInternalServerError" })
    }
})
module.exports = router