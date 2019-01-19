const router = require("koa-router")();
const mysql = require("../databse/knex");
const config = require("../config");
const fs = require("fs");
const qr = require("qr-image");
const crypto = require("crypto");

let Encrypt = (data, key) => {
  const cipher = crypto.createCipher("aes192", key);
  var crypted = cipher.update(data, "utf8", "hex");
  crypted += cipher.final("hex");
  return crypted;
};

let Decrypt = (encrypted, key) => {
  const decipher = crypto.createDecipher("aes192", key);
  var decrypted = decipher.update(encrypted, "hex", "utf8");
  decrypted += decipher.final("utf8");
  return decrypted;
};

let encrypt = function(text, key) {
  var cipher = crypto.createCipher("aes-128-ecb", key);
  var crypted = cipher.update(text, "utf8", "hex");
  crypted += cipher.final("hex");
  return crypted;
};

let decrypt = function(text, key) {
  var decipher = crypto.createDecipher("aes-128-ecb", key);
  var dec = decipher.update(text, "hex", "utf8");
  dec += decipher.final("utf8");
  return dec;
};

router.get("/create_qr", async (ctx, next) => {
  const packageid = ctx.query.id;
  const package = await mysql("package")
    .select("*")
    .where({
      id: packageid
    });
  console.log(package);
  const key = package[0].password;
  const packageInfo =
    ":" +
    package[0].destination +
    ":" +
    package[0].source +
    ":" +
    package[0].mailer +
    ":" +
    package[0].mailer_phone +
    ":" +
    package[0].receiver +
    ":" +
    package[0].receiver_phone;
  const encryptedPackageInfo = encrypt(packageInfo, key);

  const codeString = package[0].id + ":" + encryptedPackageInfo;
  try {
    var img = qr.image(codeString, { size: 10 });
    ctx.res.writeHead(200, { "Content-Type": "image/png" });
    ctx.response.type = "image/png";
    ctx.response.status = 200;
    ctx.body = img;
  } catch (e) {
    console.log(e);
    ctx.response.type = "text/html";
    ctx.response.status = 414;
    ctx.response = "<h1>414 Request-URI Too Large</h1>";
  }
});

router.get("/permission", async (ctx, next) => {
  const query = ctx.query;
  console.log(query);
  const postman_id = query.postman_id;
  const package_id = query.package_id;
  const sql = await mysql("permission")
    .select("*")
    .where({
      postman_id: postman_id,
      package_id: package_id
    });
  if (sql.length == 0) {
    ctx.body = {
      code: -1,
      msg: "Access Denied:don't have permission"
    };
  } else {
    const key = await mysql("package")
      .select("password")
      .where({
        id: package_id
      });
    console.log(key);
    ctx.body = {
      code: 1,
      msg: "Permission Granted.",
      key: key[0].password
    };
  }
});

router.get("/addpermission", async (ctx, next) => {
  const query = ctx.query;
  console.log(query);
  const postman = query.postman;
  const package = query.package;
  const operation = query.operation;
  if (operation == "ADD") {
    const sql = await mysql("postman")
      .select("*")
      .where({
        name: postman
      });
    if (sql.length == 0) {
      ctx.body = {
        code: -1,
        msg: "Postman doesn't exists."
      };
    } else {
      const packsql = await mysql("package")
        .select("*")
        .where({
          id: package
        });
      if (packsql.length === 0) {
        ctx.body = {
          code: -2,
          msg: "Package doesn't exists."
        };
      } else {
        try {
          await mysql("permission").insert({
            postman_id: postman,
            package_id: package
          });
          ctx.body = {
            code: 1,
            msg: "Successfully add permission."
          };
        } catch (e) {
          console.log(e.sqlMessage);
          ctx.body = {
            code: -3,
            msg: "Sql Error,cannot insert permission into tables."
          };
        }
      }
    }
  } else {
    try {
      await mysql("permission")
        .delete()
        .where({
          postman_id: postman,
          package_id: package
        });
      ctx.body = {
        code: 1,
        msg: "Successfully deleted."
      };
    } catch (e) {
      console.log(e.sqlMessage);
      ctx.body = {
        code: -1,
        msg: "Failed to delete:" + sqlMessage
      };
    }
  }
});

module.exports = router;
