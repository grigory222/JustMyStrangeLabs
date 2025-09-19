import { reactive } from 'vue';
import { Client } from '@stomp/stompjs';
import type { IFrame, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

type MessageHandler = (message: any) => void;

class WebSocketService {
  private client: Client | null = null;
  private handlers: Set<MessageHandler> = new Set();
  public state = reactive({ connected: false });

  connect(url = (import.meta.env.VITE_WS_URL as string) || 'http://localhost:8080/route-manager/ws'): void {
    if (this.client && this.client.connected) return;
    
    console.log('Attempting WebSocket connection to:', url);
    
    try {
      this.client = new Client({
        webSocketFactory: () => new SockJS(url),
        reconnectDelay: 2000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        debug: (str) => {
          console.log('STOMP Debug:', str);
        },
        onConnect: (frame: IFrame) => {
          console.log('Connected to WebSocket:', frame);
          this.state.connected = true;
          
          // Subscribe to route events
          this.client?.subscribe('/topic/routes', (message: IMessage) => {
            console.log('Received WebSocket message:', message.body);
            const data = JSON.parse(message.body);
            this.handlers.forEach((handler) => handler(data));
          });
        },
        onDisconnect: (frame: IFrame) => {
          console.log('Disconnected from WebSocket:', frame);
          this.state.connected = false;
        },
        onStompError: (frame: IFrame) => {
          console.error('STOMP error:', frame);
          this.state.connected = false;
        },
        onWebSocketError: (event) => {
          console.error('WebSocket error:', event);
        }
      });

      this.client.activate();
    } catch (error) {
      console.error('WebSocket connection failed:', error);
    }
  }

  disconnect(): void {
    if (this.client) {
      this.client.deactivate();
      this.client = null;
      this.state.connected = false;
    }
  }

  subscribe(handler: MessageHandler): () => void {
    this.handlers.add(handler);
    return () => this.handlers.delete(handler);
  }
}

export const wsService = new WebSocketService();


