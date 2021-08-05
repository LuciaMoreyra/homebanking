const app = Vue.createApp({
  data() {
    return {
      navShow: false,
      formName: "propias",
      accounts: [],
      amount: "",
      description: "",
        numberDestination: "",
        originAccount: "",
      toastMessage: "Transferencia realizada",
      myModal: [],
      toastSuccess: [],
      toastDanger  :[],
    };
  },
  created() {
    this.fetchAccounts();
  },
  mounted() {
    this.initializeElements();
  },
  methods: {
    toggleNav() {
      this.navShow = !this.navShow;
    },
    logout() {
      axios.post("/api/logout").then((response) => console.log(response));
      window.location = window.location.origin + "/web/index.html";
    },
    fetchAccounts() {
      axios
        .get("/api/clients/current/accounts")
        .then((res) => (this.accounts = res.data))
        .catch((error) => console.log(error));
    },
    initializeElements() {
      this.myModal = new bootstrap.Modal(
        document.getElementById("modalConfirm"),
        {
          keyboard: false,
        }
      );
      this.toastSuccess = new bootstrap.Toast(document.getElementById("toast-success"));
      this.toastDanger = new bootstrap.Toast(document.getElementById("toast-danger"))
    },
    openModal() {
      this.myModal.show();
    },
    clearForm(){
        this.amount= '';
        this.description = '';
        this.numberDestination= "";
        this.originAccount= "";
    },
    makeTransfer() {
      axios({
        method: "post",
        url: "/api/transactions",
        params: this.datosTransferencia,
        headers: {
          "content-type": "application/x-www-form-urlencoded",
        },
      })
        .then((response) => {
          console.log(response);
          this.toastMessage = "Transferencia realizada";
          this.toastBg = "bg-success";
          this.myModal.hide();
          this.toastSuccess.show();
          this.clearForm();
          this.fetchAccounts();
        })
        .catch((error) => {
          console.log(error.response);
          this.toastMessage = "Error: " + error.response.data;
          this.myModal.hide();
          this.toastDanger.show();
        });
    },
  
  },
  computed: {
      datosTransferencia(){
          return{
              amount: this.amount,
              description: this.description,
              numberOrigin: this.originAccount.number,
              numberDestination: this.numberDestination, 
          }
      }
  },
}).mount("#app");
