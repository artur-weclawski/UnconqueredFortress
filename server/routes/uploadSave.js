const router = require("express").Router()
const { Save } = require("../models/save")
const jwt = require("jsonwebtoken")

router.post("/", async (req, res) => {
    try {

        const dataFromRequest  = {login: req.headers.login , profileNumber: req.headers.profilenumber, seed: req.headers.seed, difficulty: req.headers.difficulty, finishedMaps: req.headers.finishedmaps, health: req.headers.health, maxHealth: req.headers.maxhealth, wave: req.headers.wave, gold: req.headers.gold, diamonds: req.headers.diamonds, terrainModifications: JSON.parse(req.headers.terrainmodifications), buildings: JSON.parse(req.headers.buildings), roadObstacles: JSON.parse(req.headers.roadobstacles), unlockedUpgrades: JSON.parse(req.headers.unlockedupgrades)}
        
        if (req.headers.token=="")
            return res.send({status: 401, message: "ResponseUnauthorized"})
        if (!jwt.verify( req.headers.token, process.env.JWTPRIVATEKEY))
            return res.send({status: 401, message: "ResponseUnauthorized"})

        const save = await Save.exists({ login: dataFromRequest.login, profileNumber: dataFromRequest.profileNumber });
        
        if (save)
        {
            await Save.findOneAndUpdate({ login: dataFromRequest.login, profileNumber: dataFromRequest.profileNumber }, dataFromRequest)
            return res.send({status: 200, message: "ResponseSuccessfullySaved" })
        }
        else 
        {
            await Save({ ...dataFromRequest }).save()
            return res.send({status: 200, message: "ResponseSuccessfullyCreatedSave" })
        }
    } catch (error) {
        if (error instanceof jwt.JsonWebTokenError)
            return res.send({status: 401, message: "ResponseUnauthorized"})
        res.send({status: 500, message: "ResponseInternalServerError" })
    }
})
module.exports = router