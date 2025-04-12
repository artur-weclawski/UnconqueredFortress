const mongoose = require("mongoose")
const jwt = require("jsonwebtoken")
const Joi = require("joi")
const passwordComplexity = require("joi-password-complexity")

const userSchema = new mongoose.Schema({
    login: { type: String, required: true },
    email: { type: String, required: true },
    password: { type: String, required: true }
})

userSchema.methods.generateAuthToken = function () {
    const token = jwt.sign({ _id: this._id, login: this.login }, process.env.JWTPRIVATEKEY, { expiresIn: "7d",})
    return token
}

const User = mongoose.model("User", userSchema)

const validate = (data) => {
    const schema = Joi.object({
        login: Joi.string().required().label("Login"),
        email: Joi.string().email().required().label("Email"),
        password: passwordComplexity().required().label("Password"),
    })
    return schema.validate(data)
}
module.exports = { User, validate }