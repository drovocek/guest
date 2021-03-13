// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
<style include='lumo-badge'>
        html {
      --lumo-size-xl: 4rem;
      --lumo-size-l: 3rem;
      --lumo-size-m: 2.5rem;
      --lumo-size-s: 2rem;
      --lumo-size-xs: 1.75rem;
      --lumo-space-xl: 2.5rem;
      --lumo-space-l: 1.75rem;
      --lumo-space-m: 1.125rem;
      --lumo-space-s: 0.75rem;
      --lumo-space-xs: 0.375rem;
      --lumo-font-size: 1rem;
      --lumo-font-size-xxxl: 3rem;
      --lumo-font-size-xxl: 2.25rem;
      --lumo-font-size-xl: 1.75rem;
      --lumo-font-size-l: 1.375rem;
      --lumo-font-size-m: 1.125rem;
      --lumo-font-size-s: 1rem;
      --lumo-font-size-xs: 0.875rem;
      --lumo-font-size-xxs: 0.8125rem;
      --lumo-line-height-m: 1.8;
      --lumo-line-height-s: 1.5;
      --lumo-line-height-xs: 1.3;
    }

</style>
</custom-style>

<dom-module id="theme-vaadin-button-0" theme-for="vaadin-button">
    <template>
        <style>
        
:host(:not([theme~="tertiary"])) {
  background-image: linear-gradient(var(--lumo-tint-5pct), var(--lumo-shade-5pct));
  box-shadow: inset 0 0 0 1px var(--lumo-contrast-20pct);
}
:host(:not([theme~="tertiary"]):not([theme~="primary"]):not([theme~="error"]):not([theme~="success"])) {
  color: var(--lumo-body-text-color);
}
:host([focus-ring]:not([theme~="tertiary"])) {
  box-shadow: 0 0 0 2px var(--lumo-primary-color-50pct), inset 0 0 0 1px var(--lumo-primary-color);
}
:host([focus-ring][theme~="primary"]) {
  box-shadow: 0 0 0 2px var(--lumo-primary-color-50pct), inset 0 0 0 1px var(--lumo-primary-contrast-color);
}
:host([theme~="primary"]) {
  text-shadow: 0 -1px 0 var(--lumo-shade-20pct);
}
        </style>
    </template>
</dom-module>

`;

document.head.appendChild($_documentContainer.content);
