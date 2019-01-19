const router = require("koa-router")();
const mysql = require("../databse/knex");
router.prefix("/users");

router.get("/auth", async (ctx, next) => {
  console.log("request:", ctx.query);
  const account = ctx.query.account;
  const sql = await mysql("PostMan")
    .select("name")
    .where("name", account);
  console.log(sql);
  if (sql.length == 0) {
    ctx.body = {
      code: -1,
      msg: "No such user,please register first."
    };
  } else {
    ctx.body = {
      code: 1,
      msg: "User exists."
    };
  }
});

router.get("/pass", async (ctx, next) => {
  const account = ctx.query.account;
  const password = ctx.query.password;
  const sql = await mysql("PostMan").where({
    name: account,
    password: password
  });
  console.log("sql:", sql);
  if (sql.length == 0) {
    ctx.body = {
      code: -1,
      msg: "Wrong Password."
    };
    console.log("Wrong password,access dennied.");
  } else {
    ctx.body = {
      code: 1,
      msg: "Authentificated."
    };
    console.log("right password,access permitted.");
  }
});

router.get("/register", async (ctx, next) => {
  const query = ctx.query;
  console.log(query);
  const name = query.name;
  const password = query.password;
  const sql = await mysql("postman")
    .select("name")
    .where({
      name: name
    });
  if (sql.length != 0) {
    console.log("User already exists");
    ctx.body = {
      code: 2,
      msg: "User name already exists."
    };
  } else {
    try {
      await mysql("postman").insert({
        name: name,
        password: password
      });
      ctx.body = {
        code: 1,
        msg: "Register Success."
      };
    } catch (e) {
      ctx.body = {
        code: -1,
        msg: "Register failed:" + e.sqlMessage
      };
      console.log("Failed to register:" + e.sqlMessage);
    }
  }
});

module.exports = router;
