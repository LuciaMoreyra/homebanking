const app = Vue.createApp({
  data() {
    return {
      navShow: false,
      loans: [],
       accounts: [],

      // form v-models
      selectedLoan: {},
      selectedPayment: "",
      account: "",
      amount: '',

     // modals
     modalBodyContent:"",
     myModal: [],
     modalMessage:[],
    };
  },
  created() {
    this.fetchLoans();
    this.fetchAccounts();
  },
  mounted() {
    this.myModal = new bootstrap.Modal(
      document.getElementById("modalConfirm"),
      {
        keyboard: false,
      }
    );
    this.modalMessage = new bootstrap.Modal(
          document.getElementById("modalMessage"),
          {
            keyboard: false,
          }
        );
  },
  methods: {
    toggleNav() {
      this.navShow = !this.navShow;
    },
    logout() {
      axios.post("/api/logout").then((response) => console.log(response));
      window.location = window.location.origin + "/web/index.html";
    },
    fetchLoans() {
      axios
        .get("/api/loans")
        .then((response) => {
          console.log(response);
          this.loans = response.data;
          this.loans.forEach((loan) => loan.payments.sort((a, b) => a - b)); // ordeno los payments para que se vean de menor a mayor
        })
        .catch((error) => console.log(error.response));
    },
    fetchAccounts() {
      axios
        .get("/api/clients/current/accounts")
        .then((res) => (this.accounts = res.data))
        .catch((error) => console.log(error));
    },
    openModal() {
      this.myModal.show();
    },
    createLoan() {
      axios
        .post("/api/client-loan", {
          amount: this.amount,
          destinationAccountNumber: this.account,
          loanId: this.selectedLoan.id,
          payments: this.selectedPayment,
        })

        .then((response) => {
          console.log(response)
          this.modalBodyContent = 'Préstamo asignado a la cuenta ' + this.account;
          // this.clearForm();
          this.myModal.hide();
          this.modalMessage.show();
        })
        .catch((error) => {
            console.log(error.response)
            this.modalBodyContent = 'No se pudo asignar el prestamo';
            this.myModal.hide();
            this.modalMessage.show();
        });
        },
        goToUrl(number) {
          window.location = `/web/account.html?number=${number}`;
        },

    clearForm(){
      this.amount = '';
      this.selectedLoan= {};
      this.selectedPayment= "";
      this.account= "";
    }
  },
  computed: {
    montoCuota() {
      return (
        (Number(this.amount) + (Number(this.amount) * this.selectedLoan.percentage /100)) /
        Number(this.selectedPayment)
      );
    },
  },
}).mount("#app");
