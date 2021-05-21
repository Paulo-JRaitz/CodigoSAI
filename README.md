<h1 align="center">
    <img alt="Paulo Raitz" src="https://avatars.githubusercontent.com/u/69216941?v=4" width="15%">
</h1>

<p align="center">
  <img src="https://img.shields.io/static/v1?label=license&message=MIT&color=FFCC00&labelColor=262626" alt="License" >
</p>

### 💻 Gerador de Código SAI (Sistema Abrapa de Identificação)

O algoritmo de geração do código está especificado em

```
http://www.gs1.org/docs/gsmp/barcodes/GS1_General_Specifications.pdf
```

O cálculo consiste em 3 passos:

- 1. Somar o resultado da multiplicação alternada de cada dígito por 3 e 1, ex.: 0007898536171040081 (sem o dígito verificador) 0 _ 3 + 0 _ 1, + 0 _ 3 + 7 _ 1 + 8 _ 3 + 9 _ 1... = 124.

- 2. Achar o múltiplo de 10 que seja igual ou maior ao resultado: No exemplo acima seria 130.

- 3. Diminuir o múltiplo pelo resultado obtido: 130 - 124 = 6 Dígito verificador = 6

* [Ir para Playground](https://pl.kotl.in/cG43Zq7a5)
