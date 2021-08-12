const app = Vue.createApp({
  data() {
    return {
      clientData: [],
      navShow: false,
      loans: [],
      // cards: [],
      // cardDeleteId: "",
      accountToDelete: "",
      destinationAccount: "",
      accountTypeCreate:'',
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.clientData = response.data;
          this.clientData.accounts.sort((a, b) => b.id - a.id);
          this.loans = this.clientData.loans;
          // this.cards = this.clientData.cards;
          console.log(this.clientData.accounts);
        })
        .catch((error) => console.log(error));
    },
    dateAndTime(creationdate) {
      let date = new Date(creationdate);
      return date.toLocaleDateString();
    },
    toggleNav() {
      this.navShow = !this.navShow;
    },
    goToUrl(number) {
      const actual = window.location.origin;
      window.location = actual + `/web/account.html?number=${number}`;
    },
    logout() {
      axios.post("/api/logout").then((response) => console.log(response));
      window.location = window.location.origin + "/web/index.html";
    },
    createAccount() {
      axios
        .post("/api/clients/current/accounts?type=" + this.accountTypeCreate)
        .then((response) =>
          // location.reload()
          this.fetchData()
        )
        .catch((error) => console.log(error.response));
    },
    createCard() {
      window.location = window.location.origin + "/web/create-cards.html";
    },
    // deleteCard() {
    //   console.log("eliminar tarjeta");
    //   axios
    //     .delete("/api/clients/current/cards", {
    //       params: { cardId: this.cardDeleteId },
    //     })
    //     .then((res) => {
    //       console.log(res);
    //       this.fetchData();
    //     })
    //     .catch((err) => console.log(err.response));
    // },
    nuevoPrestamo() {
      window.location = "/web/loan-application.html";
    },
    // isExpired(dateStr) {
    //   return new Date(dateStr) < new Date();
    // },
     deleteAccount() {
      console.log("eliminar cuenta " + this.accountToDelete.number);
      axios({
        method: "post",
        url: "/api/clients/current/account",
        params: {
          number: this.accountToDelete.number,
        },
        headers: {
          "content-type": "application/x-www-form-urlencoded",
        },
      })
        .then((res) => {
          console.log(res);
          this.fetchData();
          this.accountToDelete = "";
        })
        .catch((err) => console.log(err.response));
    },
    transferAndDelete() {
      axios({
        method: "post",
        url: "/api/transactions",
        params: {
          amount: this.accountToDelete.balance,
          description: "cierre de cuenta",
          numberOrigin: this.accountToDelete.number,
          numberDestination: this.destinationAccount,
        },
        headers: {
          "content-type": "application/x-www-form-urlencoded",
        },
      })
        .then((response) => {
          console.log(response);
          this.fetchData();
          // this.clearForm();
        })
        .then((response) => {
          this.deleteAccount();
        })
        .catch((error) => {
          console.log(error.response);
        });
    },
    traducir(accountType){
      if (accountType == 'CHECKING'){
        return 'Corriente';

      }
      if (accountType == 'SAVING'){
        return 'Ahorro';
      }
      return '';
    },

  },

  computed: {
    hasLoans() {
      return this.loans.length > 0;
    },
    // creditCards() {
    //   return this.cards.filter((card) => card.type == "CREDIT");
    // },
    // debitCards() {
    //   return this.cards.filter((card) => card.type == "DEBIT");
    // },
    accountsNumber() {
      if (this.clientData.accounts != undefined) {
        return this.clientData.accounts.length;
      }
    },
    // maxCards() {
    //   if (this.creditCards.length == 3 && this.debitCards.length == 3) {
    //     return true;
    //   }
    //   return false;
    // },
  },
}).mount("#app");
