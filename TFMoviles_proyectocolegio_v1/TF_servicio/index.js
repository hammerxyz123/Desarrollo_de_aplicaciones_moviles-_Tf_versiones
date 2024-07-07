const express = require('express')
const mysql = require('mysql')
const bodyParser = require('body-parser')

const app = express()
app.use(bodyParser.json())

const PUERTO = 3000

const conexion = mysql.createConnection(
    {
        host:'localhost',
        database:'colegio',
        user:'root',
        password:''
    }
)

app.listen(PUERTO, ()=>{
    console.log("Servidor corriendo en el puerto "+PUERTO)
})

conexion.connect(error => {
    if(error) throw error
    console.log("Conexión a la base de datos fue exitosa")
})

//Obtener
app.get("/festividad", (req, res) =>{
    const query = "SELECT * FROM festividad;"
    conexion.query(query, (error, resultado) =>{
        if(error) return console.error(error.message)

        const objeto = {}
        if(resultado.length > 0){
            objeto.listaFestividad = resultado
            res.json(objeto)
        }else{
            res.json("No hay registros de festividades")
        }
    })
})
app.get("/alumnos", (req, res) =>{
    const query = "SELECT * FROM alumno;"
    console.log(query)
    conexion.query(query, (error, resultado) =>{
        if(error) return console.error(error.message)

        const objeto = {}
        if(resultado.length > 0){
            objeto.listaAlumnos = resultado
            res.json(objeto)
        }else{
            res.json("No hay registros de alumnos")
        }
    })
})
app.get("/calificaciones", (req, res) =>{
    const query = "SELECT * FROM calificacion;"
    console.log(query)
    conexion.query(query, (error, resultado) =>{
        if(error) return console.error(error.message)

        const objeto = {}
        if(resultado.length > 0){
            objeto.listaCalificaciones = resultado
            res.json(objeto)
        }else{
            res.json("No hay registros de calificaciones")
        }
    })
})
app.get("/pagos", (req, res) =>{
    const query = "SELECT * FROM pago;"
    console.log(query)
    conexion.query(query, (error, resultado) =>{
        if(error) return console.error(error.message)

        const objeto = {}
        if(resultado.length > 0){
            objeto.listaPagos = resultado
            res.json(objeto)
        }else{
            res.json("No hay registros de pagos")
        }
    })
})
//Buscar
app.get("/festividad/:id",(req,res) =>{
    const {id}= req.params;
    const query ="SELECT * FROM festividad WHERE Id_festividad ="+id
    conexion.query(query,(error,resultado)=>{
        if(error) return console.error(error.message)
            
            if(resultado.length >0){
                res.json(resultado[0]);
            }else {
                res.json("No hay registros");
            }
    })
} )

//Insertar
app.post("/festividad/agregar", (req, res) =>{
    const festividades = {
        Nom_festividad: req.body.Nom_festividad,
        Desc_festividad: req.body.Desc_festividad,
        Abo_festividad: req.body.Abo_festividad,
        fech_festividad: req.body.fech_festividad,
        Obs_festividad: req.body.Obs_festividad
    }
    const query = "INSERT INTO festividad SET ?"
    console.log(query)
    conexion.query(query, festividades, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente la festividad")
    })
})
app.post("/alumnos/agregar", (req, res) =>{
    const alumnos = {
        alu_nombre: req.body.alu_nombre,
        alu_fechaNac: req.body.alu_fechaNac,
        alu_contacto: req.body.alu_contacto,
        alu_seccion: req.body.alu_seccion,
        alu_observacion: req.body.alu_observacion
    }
    const query = "INSERT INTO alumno SET ?"
    console.log(query)
    conexion.query(query, alumnos, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente el alumno")
    })
})
app.post("/calificaciones/agregar", (req, res) =>{
    const calificaciones = {
        cal_nota1: req.body.cal_nota1,
        cal_nota2: req.body.cal_nota2,
        cal_nota3: req.body.cal_nota3,
        cal_nota4: req.body.cal_nota4,
        cal_observacion: req.body.cal_observacion
    }
    const query = "INSERT INTO calificacion SET ?"
    console.log(query)
    conexion.query(query, calificaciones, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente la calificacion")
    })
})
app.post("/pagos/agregar", (req, res) =>{
    const pagos = {
        pa_pago1: req.body.pa_pago1,
        pa_pago2: req.body.pa_pago2,
        pa_pago3: req.body.pa_pago3,
        pa_pago4: req.body.pa_pago4,
        pa_observacion: req.body.pa_observacion
    }
    const query = "INSERT INTO pago SET ?"
    console.log(query)
    conexion.query(query, pagos, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente el pago")
    })
})

//Actualizar
app.put("/calificaciones/actualizar/:id",(req,res)=>{
    const {id}=req.params
    const {cal_nota1, cal_nota2,cal_nota3,cal_nota4,cal_observacion}=req.body
    const query="UPDATE calificacion SET cal_nota1= "
    +cal_nota1+", cal_nota2="+cal_nota2+", cal_nota3="+cal_nota3
    +", cal_nota4="+cal_nota4+", cal_observacion='"+cal_observacion+"' WHERE cal_id="+id+";"
    console.log(query)
    conexion.query(query,(error)=>{
        if(error)
            return console.error(error.message)
        res.json("se actualizó correctamente el calificacion")
    })
})
app.put("/alumnos/actualizar/:id",(req,res)=>{
    const {id}=req.params
    const {alu_nombre, alu_fechaNac,alu_contacto,alu_seccion,alu_observacion}=req.body
    const query="UPDATE alumno SET alu_nombre= '"
    +alu_nombre+"', alu_fechaNac='"+alu_fechaNac+"', alu_contacto='"+alu_contacto+
    "', alu_seccion='"+alu_seccion+"', alu_observacion='"+alu_observacion+"' WHERE alu_id="+id+";"
    console.log(query)
    conexion.query(query,(error)=>{
        if(error)
            return console.error(error.message)
        res.json("se actualizó correctamente el alumno")
    })
})
app.put("/pagos/actualizar/:id",(req,res)=>{
    const {id}=req.params
    const {pa_pago1, pa_pago2,pa_pago3,pa_pago4,pa_observacion}=req.body
    const query="UPDATE pago SET pa_pago1= "
    +pa_pago1+", pa_pago2="+pa_pago2+", pa_pago3="+pa_pago3
    +", pa_pago4="+pa_pago4+", pa_observacion='"+pa_observacion+"' WHERE pa_id="+id+";"
    console.log(query)
    conexion.query(query,(error)=>{
        if(error)
            return console.error(error.message)
        res.json("se actualizó correctamente el pago")
    })
})

//Eliminar
app.delete("/alumnos/eliminar/:id",(req,res)=>{
    const {id}=req.params
    const query="DELETE FROM alumno WHERE alu_id= "+id+";"
    console.log(query)
    conexion.query(query,(error)=>{
        if(error) console.error(error.message)
        res.json("Se elimino correctamente el alumno")
    })
})
app.delete("/calificaciones/eliminar/:id",(req,res)=>{
    const {id}=req.params
    const query="DELETE FROM calificacion WHERE cal_id= "+id+";"
    console.log(query)
    conexion.query(query,(error)=>{
        if(error) console.error(error.message)
        res.json("Se elimino correctamente la calificacion")
    })
})
app.delete("/pagos/eliminar/:id",(req,res)=>{
    const {id}=req.params
    const query="DELETE FROM pago WHERE pa_id= "+id+";"
    console.log(query)
    conexion.query(query,(error)=>{
        if(error) console.error(error.message)
        res.json("Se elimino correctamente el pago")
    })
})