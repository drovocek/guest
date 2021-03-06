import {html, PolymerElement} from '@polymer/polymer/polymer-element';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field/vaadin-email-field';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-custom-field';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';

class GetPassView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        // language=HTML
        return html`
            <h3>Pass info</h3>
            <vaadin-form-layout style="width: 100%;">
                <vaadin-vertical-layout>
                    <vaadin-text-field label="Registration number" id="regNum"></vaadin-text-field>
                    <vaadin-date-picker label="Arrival date" id="arrivalDate"></vaadin-date-picker>
                </vaadin-vertical-layout>
            </vaadin-form-layout>
            <vaadin-horizontal-layout
                    style="margin-top: var(--lumo-space-m); margin-bottom: var(--lumo-space-l);"
                    theme="spacing"
            >
                <vaadin-button theme="primary success small" id="get" on-click="createCarPass">Get</vaadin-button>
                <vaadin-button theme="small" id="clear" on-click="clearForm">Clear</vaadin-button>
            </vaadin-horizontal-layout>
        `;
    }

    static get is() {
        return 'get-pass-view';
    }
}

customElements.define(GetPassView.is, GetPassView);
