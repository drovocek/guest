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
        // language=HTML
        return html`
            <vaadin-form-layout>
                <vaadin-form-item>
                    <h3>Company</h3>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-text-field label="Name" id="name"></vaadin-text-field>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-email-field label="Email" id="email"></vaadin-email-field>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-text-field label="Phone"
                                       id="phone"
                                       pattern="\\d*"
                                       prevent-invalid-input
                    ></vaadin-text-field>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-password-field label="Enter password" id="enterPass"></vaadin-password-field>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-password-field label="Confirm password" id="confirmPass"></vaadin-password-field>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-horizontal-layout class="button-layout">
                        <vaadin-button class="enter" id="enter" on-click="save">Save</vaadin-button>
                        <vaadin-button class="clear" id="clear" on-click="clearForm">Clear</vaadin-button>
                    </vaadin-horizontal-layout>
                </vaadin-form-item>
            </vaadin-form-layout>
        `;
    }

    static get is() {
        return 'registration-view';
    }
}

customElements.define(RegistrationView.is, RegistrationView);
