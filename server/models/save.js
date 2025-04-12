const mongoose = require("mongoose")

const saveSchema = new mongoose.Schema({
    login: { type: String, required: true },
    profileNumber: {type: Number, required: true},
    seed: { type: Number, required: true },
    difficulty: { type: String, required: true },
    finishedMaps: { type: Number, required: true },
    health: { type: Number, required: true },
    maxHealth: { type: Number, required: true },
    wave: { type: Number, required: true },
    gold: { type: Number, required: true },
    diamonds: {type: Number, required: true},
    terrainModifications: {type: Array, required: true},
    buildings: {type: Array, required: true},
    roadObstacles: {type: Array, required: true},
    unlockedUpgrades: {type: Array, required: true}
})

const Save = mongoose.model("Save", saveSchema)

module.exports = { Save }