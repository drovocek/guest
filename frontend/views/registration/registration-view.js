import {html, PolymerElement} from '@polymer/polymer/polymer-element';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field/vaadin-email-field';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-custom-field';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';

class RegistrationView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        return html`
      <h3>Company</h3>
      <vaadin-form-layout style="width: 100%;">
      <vaadin-vertical-layout>
        <vaadin-text-field label="Name" id="name"></vaadin-text-field>
        <vaadin-email-field label="Email" id="email" ></vaadin-email-field>
        <vaadin-text-field label="Phone"
              id="phone"
              style="flex-grow: 1;"
              pattern="\\d*"
              prevent-invalid-input
            ></vaadin-text-field>
          <vaadin-password-field label="Enter password" id="enterPass"> </vaadin-password-field>
          <vaadin-password-field label="Confirm password" id="confirmPass"> </vaadin-password-field>
          </vaadin-vertical-layout> 
      </vaadin-form-layout>
      <vaadin-horizontal-layout
        style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-l);"
        theme="spacing"
      >
        <vaadin-button theme="primary" id="save"> Save </vaadin-button>
        <vaadin-button id="cancel"> Cancel </vaadin-button>
      </vaadin-horizontal-layout>
    `;
    }

    static get is() {
        return 'registration-view';
    }
}

customElements.define(RegistrationView.is, RegistrationView);
