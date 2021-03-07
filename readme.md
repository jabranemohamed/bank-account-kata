# SG Bank Account Kata exercice
[![Build Status](https://travis-ci.org/jabranemohamed/bank-account-kata.svg?branch=master)](https://travis-ci.org/github/jabranemohamed/bank-account-kata)
[![codecov](https://codecov.io/gh/jabranemohamed/bank-account-kata/branch/master/graph/badge.svg)](https://codecov.io/gh/jabranemohamed/bank-account-kata)

<p align="center">
    <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v11-orange.svg" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring%20Boot-v2.4.3-brightgreen.svg" />
    </a>
    <a alt="Bootstrap">
        <img src="https://img.shields.io/badge/H2 database-v1.4-yellowgreen.svg">
    </a>
    <a alt="Dependencies">
        <img src="https://img.shields.io/badge/dependencies-up%20to%20date-brightgreen.svg" />
    </a>
    <a alt="Contributions">
        <img src="https://img.shields.io/badge/contributions-welcome-orange.svg" />
    </a>
</p>

### Used Tools/Frameworks
- Java 11 
- Intellij Ultimate 2020.3
- Spring Boot 2.4.3
- I tried to use similar Technologies used in the Marked to make the demo little bit real.

### Installing 
- Download the project from Github
- Import the project in Intellij   
- Execute the main class **BankAccountKataApplication** using **_demo_** profile as an argument in Intellij

### Result and Demo
- You can see the exercice resulat in the console or in browser 
- Import the project in Intellij
- Execute the main class **BankAccountKataApplication**

### Some Screenshot
- Result in the console
  <img src="https://github.com/jabranemohamed/bank-account-kata/blob/master/img/console.png" alt="console">
  <img src="https://github.com/jabranemohamed/bank-account-kata/blob/master/img/console2.png" alt="console">

- Result in the console
  <img src="https://github.com/jabranemohamed/bank-account-kata/blob/master/img/account.png" alt="account">
  <img src="https://github.com/jabranemohamed/bank-account-kata/blob/master/img/transaction.png" alt="transaction">

### Troubleshooting
- If you need to access to the H2 db in the browser, the URL is printed in the console, here an example
  `H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:5b22f60f-cbf1-4873-a84b-3da0be2e0788'
`
- The default username is "sa", there are no password.

### Possible improvements 
- Introduce of possiblity of overdraft "dépassement de découvert" when withdrawal operation 
- Showing Pending Operation currently they pass directly to "ACCEPTED"
- From design we may think about Transaction history for performance question  

## Thank you 
