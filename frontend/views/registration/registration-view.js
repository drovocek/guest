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

// <h3>Personal information</h3>
// <vaadin-form-layout style="width: 100%;">
//     <vaadin-text-field label="First name" id="firstName"></vaadin-text-field>
//     <vaadin-text-field label="Last name" id="lastName"></vaadin-text-field>
//     <vaadin-date-picker id="birthday" label="Birthday"></vaadin-date-picker>
//     <vaadin-custom-field id="phoneNumber" label="Phone number">
//         <vaadin-horizontal-layout theme="spacing">
//             <vaadin-combo-box
//                 id="pnCountryCode"
//                 style="width: 120px;"
//                 pattern="\+\d*"
//                 placeholder="Country"
//                 prevent-invalid-input
//             ></vaadin-combo-box>
//             <vaadin-text-field
//                 id="pnNumber"
//                 style="flex-grow: 1;"
//                 pattern="\d*"
//                 prevent-invalid-input
//             ></vaadin-text-field>
//         </vaadin-horizontal-layout>
//     </vaadin-custom-field>
//     <vaadin-email-field id="email" label="Email address"></vaadin-email-field>
//     <vaadin-text-field id="occupation" label="Occupation"></vaadin-text-field>
// </vaadin-form-layout>
// <vaadin-horizontal-layout
//     style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-l);"
//     theme="spacing"
// >
//     <vaadin-button theme="primary" id="save"> Save </vaadin-button>
//     <vaadin-button id="cancel"> Cancel </vaadin-button>
// </vaadin-horizontal-layout>