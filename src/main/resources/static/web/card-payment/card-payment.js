const app = Vue.createApp({
  data() {
    return {
      number: "",
      cvv: "",
      amount: "",
      description: "",
      modalError:[],
      modalSuccess:[],
      errorMessage : '',
    };
  },
  mounted(){
this.initializeElements();
  },
  methods: {
    pay() {
      axios
        .post("/api/payment", {
          cardNumber: this.number,
          cvv: this.cvv,
          amount: this.amount,
          description: this.description,
        })
        .then((res) => {
          console.log(res);
          console.log(res.status);
          this.modalSuccess.show();
          this.clearForm();
        })
        .catch((err) => {
          console.log(err.response);
          this.errorMessage = err.response.data
          this.modalError.show();
          console.log(err.response.status);
        });
    },
    clearForm(){
      this.number= "";
      this.cvv= "";
      this.amount= "";
      this.description= "";
    },
    initializeElements() {
      this.modalError = new bootstrap.Modal(
        document.getElementById("modal-error"),
        {
          keyboard: false,
        }
      );
      this.modalSuccess = new bootstrap.Modal(
        document.getElementById("modal-success"),
        {
          keyboard: false,
        }
      );
     
    },
  },
}).mount("#app");
