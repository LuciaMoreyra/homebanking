const app = Vue.createApp({
  data() {
    return {
      loginData:{
        email: "",
        password: "",
      
      },
      signUpData:{
        firstName:'',
        lastName:'',
        email:'',
        password:'',
      },
      mensaje:'',
      formulario:'signin',
    };
  },
  methods: {
    login() {
      axios({
        method: "post",
        url: "/api/login",
        params:{ 
          email:this.loginData.email,
          password: this.loginData.password,
        },
        headers: {
          "content-type": "application/x-www-form-urlencoded",
        },
      })
      .then((response) => {
      if (this.loginData.email === 'admin@admin'){
      window.location = '/web/manager/manager.html';
       }else{
        window.location = window.location.origin + '/web/accounts.html';
       }

      })
      .catch(error => {
        if (error.response){
            console.log(error.response);
            this.mensaje = 'Error: los datos no son validos';
        }  
        else if (error.request){
            console.log(error.request);
        }
        else{
            console.log('Error:' + error.message)
        }
        console.log(error.config)
        });
    },
    signup(){
      axios({
        method:'post',
        url:'/api/clients',
        params:{
          firstName: this.signUpData.firstName,
          lastName: this.signUpData.lastName,
          email: this.signUpData.email,
          password: this.signUpData.password,
        },
        headers:{
          'content-type':'application/x-www-form-urlencoded'
        },
      }).then(response => {
        this.loginData.email =  this.signUpData.email;
        this.loginData.password = this.signUpData.password;
        this.login();
      })
      .catch(error => {
        if (error.response){
            console.log(error.response);
            this.mensaje = 'El email ingresado ya existe';
        }  
        else if (error.request){
            console.log(error.request);
        }
        else{
            console.log('Error:' + error.message)
        }
        console.log(error.config)
        });

    },  
    changeForm(formName){
      this.formulario = formName;
      this.mensaje = '';
    },  
  },
});
app.mount("#app");
