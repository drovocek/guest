import {html, PolymerElement} from '@polymer/polymer/polymer-element';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field/vaadin-email-field';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-custom-field';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';

class CardView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        // language=HTML
        return html`
            <vaadin-form-layout>
                <vaadin-form-item>
                    <h3>Pass info</h3>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-text-field label="Registration number" id="regNum"></vaadin-text-field>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-date-picker label="Arrival date" id="arrivalDate"></vaadin-date-picker>
                </vaadin-form-item>
                <br>
                <vaadin-form-item>
                    <vaadin-horizontal-layout class="button-layout">
                        <vaadin-button class="enter" id="enter" on-click="save">Get</vaadin-button>
                        <vaadin-button class="clear" id="clear" on-click="clearForm">Clear</vaadin-button>
                    </vaadin-horizontal-layout>
                </vaadin-form-item>
            </vaadin-form-layout>
        `;
    }

    static get is() {
        return 'get-pass-view';
    }
}

customElements.define(GetPassView.is, GetPassView);
