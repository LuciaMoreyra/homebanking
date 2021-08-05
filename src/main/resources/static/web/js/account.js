const app = Vue.createApp({
  data() {
    return {
      account: [],
      accountNumber: "",
      transactions: [],
      navShow: false,
      params: {},
      dateStart: "",
      dateEnd: "",
    };
  },
  created() {
    const urlSearchParams = new URLSearchParams(window.location.search);
    this.params = Object.fromEntries(urlSearchParams.entries());
    console.log(this.params);
    this.accountNumber = this.params.number;
    this.fetchAccount();
    this.params.to = "";
    this.params.from = "";
    this.fetchTransactions();
  },
  methods: {
    fetchTransactions() {
      axios
        .get("/api/transactions", { params: this.params })
        .then((response) => {
          this.transactions = response.data;
          this.transactions.sort((a, b) => new Date(b.date) - new Date(a.date));
          console.log(this.transactions);
        })
        .catch((error) => console.log(error.response));
    },
    filter() {
      this.params.to = this.dateEnd;
      this.params.from = this.dateStart;
      this.fetchTransactions();
    },
    fetchAccount() {
      axios
        .get("/api/clients/current/account", {
          params: { number: this.accountNumber },
        })
        .then((response) => {
          this.account = response.data;
          console.log(this.account);
        })
        .catch((error) => console.log(error.response));
    },
    objectClass(type) {
      return {
        "bg-success": type == "CREDIT",
        "bg-danger": type == "DEBIT",
      };
    },
    toggleNav() {
      this.navShow = !this.navShow;
    },
    logout() {
      axios.post("/api/logout").then((response) => console.log(response));
      window.location = window.location.origin + "/web/index.html";
    },
    dateAndTime(creationdate) {
      let date = new Date(creationdate);
      return date.toLocaleString();
    },
    formatDate(str) {
      return new Date(str + " ").toLocaleDateString();
    },
    formatARS(balance) {
      if (balance == null) {
        return "";
      }
      let amount = new Intl.NumberFormat("es-AR", {
        style: "currency",
        currency: "ARS",
      });
      return amount.format(balance);
    },
    openPdf() {
      var dd = {
        header: {
           text:'',
          // "Transacciones de la cuenta " +
          // this.accountNumber,
           margin: 30,
        },
        footer: {
          columns: [
            {
              text: "impreso el " + new Date().toLocaleDateString(),
              alignment: "right",
              margin: 30,
            },
          ],
        },
        content: [
          {
            layout: "lightHorizontalLines", // optional
            margin: [0, 20],
            table: {
              headerRows: 1,
              widths: ["auto", "auto", "*", "auto", "auto"],

              body: [
                [
                  "Descripción",
                  "Tipo",
                  "Fecha y hora",
                  "Monto",
                  "Saldo restante",
                ],
              ],
            },
          },
        ],
        pageSize: "A4",
        pageMargins: [40, 60, 40, 60],
      };

      if (app.params.to != "" && app.params.from != "") {
        dd.header.text +=
          " desde " +
          this.formatDate(this.dateStart) +
          " hasta " +
          this.formatDate(this.dateEnd);
      }

      this.transactions.forEach((h) => {
        let aux = [];
        aux.push(h.description);
        aux.push(h.type);

        aux.push(new Date(h.date).toLocaleString());
        aux.push({ text: h.amount, bold: true });
        aux.push(this.formatARS(h.finalBalance));
        dd.content[0].table.body.push(aux);
      });

      dd.content.unshift({
        text:
          "creada el dia : " +
          new Date(this.account.creationDate).toLocaleDateString(),
        bold: true,
      });
      dd.content.unshift("tipo de cuenta : " + this.account.accountType);
      dd.content.unshift(
        "balance actual : " + this.formatARS(this.account.balance)
      );
      dd.content.unshift("Transacciones de la cuenta " + this.accountNumber);
      pdfMake.createPdf(dd).open();
    },
  },
  computed: {
    hasTransactions() {
      return this.transactions.length > 0;
    },
  },
}).mount("#app");
